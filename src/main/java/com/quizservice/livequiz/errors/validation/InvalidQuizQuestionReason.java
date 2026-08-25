package com.quizservice.livequiz.errors.validation;

public enum InvalidQuizQuestionReason {
	TRUE_FALSE_HAS_ANSWERABLES /*una domanda true/false non può avere un array di answerables*/,
	
	TRUE_FALSE_MISSING_ANSWERS,
	TRUE_FALSE_INVALID_ANSWERS_COUNT /*il formato di answers deve essere esattamente così
	1) [0] falso, 2) [1] vero*/,
	TRUE_FALSE_INVALID_ANSWER /*quando specifico un valore diverso da 0 e da 1*/,
	
	OPEN_QUESTION_HAS_ANSWERABLES,
	OPEN_QUESTION_HAS_ANSWERS /*una domanda aperta non può avere né possibili risposte,
	ne risposte effettive, per tanto devono essere proprietà null*/,
	
	CLOSED_QUESTION_HAS_NOT_ANSWERABLES_OR_ANSWERS /*quando non specifico opzioni o risposte*/,
	CLOSED_QUESTION_HAS_NOT_ANSWERS /*quando non specifico risposte effettive a una domanda*/,
	CLOSED_QUESTION_HAS_MORE_ANSWERS_THAN_OPTIONS /*quando specifico più risposte esatte delle effettiva risposte selezionabili*/,
	
	/*CLOSED_QUESTION_HAS_ONE_OR_MORE_UNANSWERABLES */
	/*quando specifico una o più risposte effettive che non sono contenute nelle
	opzioni di risposta*/
	/*questo code l'ho commentato perché, anziché dire semplicemente il codice di errore, preferisco
	 * specificare l'array di unanswerable questions
	*/
	
}
