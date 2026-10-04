package com.quizservice.livequiz.models.kafka;

import java.util.ArrayList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties.Json;
import org.springframework.http.codec.json.Jackson2JsonEncoder;

import com.quizservice.livequiz.models.httpRequests.QuestionAnswerable;
import com.quizservice.livequiz.models.httpRequests.QuizQuestion;
import com.quizservice.livequiz.services.QuizService;

import ch.qos.logback.classic.encoder.JsonEncoder;

public class QuizNewVersionReleasedEventMaker {

	public static QuizNewVersionAvailableEvent make(UUID quizId, short newVersion, ArrayList<QuizQuestion> addedQuestions) {
		
		QuizNewVersionAvailableEvent event = new QuizNewVersionAvailableEvent(quizId, newVersion);
		ArrayList<QuestionIndexing> addedQuestionsIndexing = new ArrayList<QuestionIndexing>();
		
		if(addedQuestions != null) {
			for(short i = 0; i < addedQuestions.size(); i++) {
				QuizQuestion currQuestion = addedQuestions.get(i);
				ArrayList<Short> optionsIndexes = null;
				
				if(currQuestion.getAnswerables() != null) {
					optionsIndexes = new ArrayList<Short>();
					
					for(byte j = 0; j < currQuestion.getAnswerables().size(); j++) {
						QuestionAnswerable currOption = currQuestion.getAnswerables().get(j);
						optionsIndexes.add(currOption.getIndex());
					}
				}
				
				QuestionIndexing piece = new QuestionIndexing(currQuestion.getIndex(), currQuestion.getType(), optionsIndexes, currQuestion.getAnswers());
				addedQuestionsIndexing.add(piece);
			}
			
			event.setAddedQuestions(addedQuestionsIndexing);
		}
		
		return event;
		
	}
	
}
