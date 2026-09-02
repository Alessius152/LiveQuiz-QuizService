package com.quizservice.livequiz.repositories;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.client.result.UpdateResult;
import com.quizservice.livequiz.models.database.QuizDocumentModel;
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
	
}
