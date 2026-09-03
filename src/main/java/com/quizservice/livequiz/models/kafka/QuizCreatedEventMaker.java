package com.quizservice.livequiz.models.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.quizservice.livequiz.models.database.QuizDocumentModel;
import com.quizservice.livequiz.models.httpRequests.QuestionAnswerable;
import com.quizservice.livequiz.models.httpRequests.QuestionType;
import com.quizservice.livequiz.models.httpRequests.QuizQuestion;

public class QuizCreatedEventMaker {

	public static QuizCreatedEvent make(QuizDocumentModel model) {
		ArrayList<QuestionIndexing> questionsIndexing = new ArrayList<QuestionIndexing>();
		ArrayList<QuizQuestion> questions = model.getQuestions();
		
		QuizCreatedEvent event = new QuizCreatedEvent(model.getQuizId(), model.getCreatorId(), questionsIndexing);

		System.out.println(questions.toString());
		if((questions == null) || questions.isEmpty()) {
			return event;
		}
		
		for(short i = 0; i < questions.size(); i++) {
			QuizQuestion q = questions.get(i);
			QuestionType qType = q.getType();

			ArrayList<Short> options = new ArrayList<Short>();
			ArrayList<Short> answers = new ArrayList<Short>();
			
			if(qType == QuestionType.TRUE_FALSE) {
				answers.add(q.getAnswers().get(0));
			}
			else if(qType == QuestionType.CLOSED_QUESTION) {
				ArrayList<QuestionAnswerable> answerables = q.getAnswerables();
				for (QuestionAnswerable ans : answerables) {
				    options.add(ans.getIndex());
				}
				answers = q.getAnswers();
			}
			
			QuestionIndexing indexing = new QuestionIndexing(q.getIndex(), qType, options.isEmpty() ? null : options, answers.isEmpty() ? null : answers);
			questionsIndexing.add(indexing);
			
		}
		
		return event;
	}
	
}
