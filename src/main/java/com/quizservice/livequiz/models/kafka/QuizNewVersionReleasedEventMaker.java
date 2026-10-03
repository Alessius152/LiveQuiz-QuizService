package com.quizservice.livequiz.models.kafka;

import java.util.ArrayList;
import java.util.UUID;

import com.quizservice.livequiz.models.httpRequests.QuestionAnswerable;
import com.quizservice.livequiz.models.httpRequests.QuizQuestion;

public class QuizNewVersionReleasedEventMaker {

	public static QuizNewVersionAvailableEvent make(UUID quizId, short newVersion, ArrayList<QuizQuestion> addedQuestions) {
		
		QuizNewVersionAvailableEvent event = new QuizNewVersionAvailableEvent(quizId, newVersion);
		
		if(addedQuestions != null) {
			ArrayList<QuestionIndexing> indexing = new ArrayList<QuestionIndexing>();
			
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
				indexing.add(piece);
			}
		}
		
		return event;
		
	}
	
}
