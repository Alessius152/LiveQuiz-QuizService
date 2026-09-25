package com.quizservice.livequiz.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.client.result.UpdateResult;
import com.quizservice.livequiz.models.database.QuizDocumentModel;
import com.quizservice.livequiz.models.database.summaries.FetchQuizSummaryModel;
import com.quizservice.livequiz.models.database.summaries.FetchQuizzesListSummaryModel;
import com.quizservice.livequiz.models.httpRequests.QuizQuestion;

public class QuizRepositoryCustomImpl implements QuizRepositoryCustom {

	private final MongoTemplate mongoTemplate;
	
	public QuizRepositoryCustomImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	public UpdateResult pushQuestions (String creatorId, UUID quizId, ArrayList<QuizQuestion> questions) {
		
		Query query = new Query(Criteria.where("creatorId").is(creatorId).and("quizId").is(quizId));
		Update update = new Update().push("questions").each(questions);
		UpdateResult result = mongoTemplate.updateFirst(query, update, QuizDocumentModel.class);
		
		return result;
		
	}
	
	public FetchQuizSummaryModel getQuiz(UUID quizId) {
		Query query = new Query(Criteria.where("quizId").is(quizId));
		
		query.fields()
			.include("name").include("description").include("creatorId")
			.include("questions.index").include("questions.question")
			.include("questions.type").include("questions.answerables");
		
		return mongoTemplate.findOne(query, FetchQuizSummaryModel.class);
	}
	
	public Page<FetchQuizzesListSummaryModel> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    ) {

        Criteria criteria = Criteria.where("name")
                .regex(Pattern.quote(name), "i");

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),

                Aggregation.project()
                        .and("name").as("name")
                        .and("description").as("description")
                        .and("creatorId").as("creatorId")
                        .and("quizId").as("quizId")
                        .and("questions").size().as("questionCount")
                        .andExpression("size(questions) * 30").as("quizTime"),

                Aggregation.skip(pageable.getOffset()),
                Aggregation.limit(pageable.getPageSize())
        );

        AggregationResults<FetchQuizzesListSummaryModel> results =
                mongoTemplate.aggregate(
                        aggregation,
                        QuizDocumentModel.class,
                        FetchQuizzesListSummaryModel.class
                );

        List<FetchQuizzesListSummaryModel> content =
                results.getMappedResults();

        long total = mongoTemplate.count(
                Query.query(criteria),
                QuizDocumentModel.class
        );

        return new PageImpl<>(
                content,
                pageable,
                total
        );
    }
	
}
