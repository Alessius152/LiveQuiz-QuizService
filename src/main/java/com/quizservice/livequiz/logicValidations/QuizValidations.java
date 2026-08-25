package com.quizservice.livequiz.logicValidations;

import java.util.ArrayList;
import java.util.HashMap;

import com.quizservice.livequiz.errors.validation.InvalidQuizQuestionReason;
import com.quizservice.livequiz.models.httpRequests.QuestionAnswerable;
import com.quizservice.livequiz.models.httpRequests.QuestionType;
import com.quizservice.livequiz.models.httpRequests.QuizQuestion;

public class QuizValidations {
	
	public static HashMap<String, ArrayList<InvalidQuizQuestionReason>> validateQuestions(ArrayList<QuizQuestion> questions) {

		HashMap<String, ArrayList<InvalidQuizQuestionReason>> invalids = new HashMap<String, ArrayList<InvalidQuizQuestionReason>>();
		
		for(int i = 0; i < questions.size(); i++) {
			QuizQuestion question = questions.get(i);
			QuestionType type = question.getType();
			ArrayList<QuestionAnswerable> answerables = question.getAnswerables();
			ArrayList<Short> answers = question.getAnswers();
			
			ArrayList<InvalidQuizQuestionReason> reasons = new ArrayList<InvalidQuizQuestionReason>();
			
			boolean isBinary = (type == null) || (type == QuestionType.TRUE_FALSE);
			boolean isOpen = type == QuestionType.OPEN_QUESTION;
			
			if(isBinary) {
				if(answerables != null) reasons.add(InvalidQuizQuestionReason.TRUE_FALSE_HAS_ANSWERABLES);
				
				if(answers == null) reasons.add(InvalidQuizQuestionReason.TRUE_FALSE_MISSING_ANSWERS);
				else {
					if(answers.size() != 1) reasons.add(InvalidQuizQuestionReason.TRUE_FALSE_INVALID_ANSWERS_COUNT);
					else {
						short ans = answers.get(0);
						if((ans != 0) && (ans != 1)) {
							reasons.add(InvalidQuizQuestionReason.TRUE_FALSE_INVALID_ANSWER);
						}
					}
				}
				
			}
			else if(isOpen) {
				if(answerables != null) reasons.add(InvalidQuizQuestionReason.OPEN_QUESTION_HAS_ANSWERABLES);
				if(answers != null) reasons.add(InvalidQuizQuestionReason.OPEN_QUESTION_HAS_ANSWERS);
			}
			else {
				if((answerables == null) || (answers == null)) reasons.add(InvalidQuizQuestionReason.CLOSED_QUESTION_HAS_NOT_ANSWERABLES_OR_ANSWERS);
				else {
					if(answers.size() == 0) reasons.add(InvalidQuizQuestionReason.CLOSED_QUESTION_HAS_NOT_ANSWERS);
					if(answers.size() > answerables.size()) reasons.add(InvalidQuizQuestionReason.CLOSED_QUESTION_HAS_MORE_ANSWERS_THAN_OPTIONS);
				}
			}
			
			if(reasons.size() > 0) {
				invalids.put(String.valueOf(i), reasons);
			}
			
		}
		
		return invalids;
		
	}
	
	public static HashMap<String, ArrayList<Short>> getUnanswerables(ArrayList<QuizQuestion> questions){
		
		HashMap<String, ArrayList<Short>> unanswerablesMap = new HashMap<String, ArrayList<Short>>();
		
		for(short i = 0; i < questions.size(); i++) {
			QuizQuestion question = questions.get(i);
			
			ArrayList<QuestionAnswerable> answerables = question.getAnswerables();
			ArrayList<Short> answers = question.getAnswers();
			
			boolean isClosedQuiz = question.getType() != QuestionType.CLOSED_QUESTION;
			
			if(isClosedQuiz || (answerables == null) || (answers == null)) {
				continue;
			}
			
			ArrayList<Short> unanswerables = new ArrayList<Short>();
			
			for(short answer : answers) {
				boolean isAnswerable = false;
				
				for(QuestionAnswerable option : answerables) {
					if(option.getIndex() == answer) {
						isAnswerable = true;
					}
				}
				
				if(isAnswerable == false) {
					unanswerables.add(answer);
				}
			}
			
			if(unanswerables.size() > 0) {
				unanswerablesMap.put(String.valueOf(i), unanswerables);
			}
		}
		
		return unanswerablesMap;
	}
	
	
}
