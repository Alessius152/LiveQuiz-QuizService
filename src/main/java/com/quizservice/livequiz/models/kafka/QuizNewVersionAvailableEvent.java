package com.quizservice.livequiz.models.kafka;

import java.util.ArrayList;
import java.util.UUID;

public class QuizNewVersionAvailableEvent {
	
	private UUID quizId;
	private short releaseNumber;
	
	private ArrayList<QuestionIndexing> addedQuestions = null;
	
	public QuizNewVersionAvailableEvent() {	};
	
	public QuizNewVersionAvailableEvent(UUID quizId, short releaseNumber) {
		this.quizId = quizId;
		this.releaseNumber = releaseNumber;
	}
	
	public QuizNewVersionAvailableEvent(UUID quizId, short releaseNumber, ArrayList<QuestionIndexing> addedQuestions) {
		this.quizId = quizId;
		this.releaseNumber = releaseNumber;
		this.addedQuestions = addedQuestions;
	}

	public UUID getQuizId() {
		return quizId;
	}

	public void setQuizId(UUID quizId) {
		this.quizId = quizId;
	}

	public short getReleaseNumber() {
		return releaseNumber;
	}

	public void setReleaseNumber(short releaseNumber) {
		this.releaseNumber = releaseNumber;
	}

	public ArrayList<QuestionIndexing> getAddedQuestions() {
		return addedQuestions;
	}

	public void setAddedQuestions(ArrayList<QuestionIndexing> addedQuestions) {
		this.addedQuestions = addedQuestions;
	}
	
}
