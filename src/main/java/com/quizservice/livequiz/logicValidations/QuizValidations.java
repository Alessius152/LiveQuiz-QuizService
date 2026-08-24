package com.quizservice.livequiz.logicValidations;

import java.util.ArrayList;

import com.quizservice.livequiz.errors.validation.InvalidQuizQuestionError;
import com.quizservice.livequiz.errors.validation.InvalidQuizQuestionReason;
import com.quizservice.livequiz.models.httpRequests.QuestionAnswerable;
import com.quizservice.livequiz.models.httpRequests.QuestionType;
import com.quizservice.livequiz.models.httpRequests.QuizQuestion;

public class QuizValidations {
	
	public static ArrayList<InvalidQuizQuestionError> validateQuestions(ArrayList<QuizQuestion> questions) {

		ArrayList<InvalidQuizQuestionError> invalidQuestions = new ArrayList<InvalidQuizQuestionError>();
		
		for(int i = 0; i < questions.size(); i++) {
			QuizQuestion question = questions.get(i);
			QuestionType type = question.getType();
			ArrayList<QuestionAnswerable> answerables = question.getAnswerables();
			ArrayList<Short> answers = question.getAnswers();
			
			InvalidQuizQuestionError error = null;
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
					if(!validateAnswers(answerables, answers)) reasons.add(InvalidQuizQuestionReason.CLOSED_QUESTION_HAS_ONE_OR_MORE_UNANSWERABLES);
				}
			}
			
			if(reasons.size() > 0) {
				error = new InvalidQuizQuestionError((short) i, reasons);
				invalidQuestions.add(error);
			}
			
		}
		
		return invalidQuestions;
		
	}
	
	public static boolean validateAnswers(ArrayList<QuestionAnswerable> answerables, ArrayList<Short> answers) {
		/*TODO: Qui mi fermo subito, alla prima domanda unanswerable, vorrei inviare la lista di risposte effettive non opzionate
		 * al client in modo che possa visionarle.*/
		for(Short answer : answers) {
			boolean isAnswerable = false;
			for(QuestionAnswerable option : answerables) {
				if(option.getIndex() == answer) {
					isAnswerable = true;
				}
			}
			if(isAnswerable == false) {
				return false;
			}
		}
		
		return true;
	}
	
}
