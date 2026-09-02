package com.quizservice.livequiz.models.kafka;

import com.quizservice.livequiz.models.database.QuizDocumentModel;

public class QuizCreatedEventMaker {

	public static QuizCreatedEvent make(QuizDocumentModel model) {
		QuizCreatedEvent event = new QuizCreatedEvent(model.getQuizId(), model.getCreatorId());
		
		return event;
	}
	
}
