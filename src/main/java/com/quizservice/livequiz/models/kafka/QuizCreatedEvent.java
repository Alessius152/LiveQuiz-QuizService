package com.quizservice.livequiz.models.kafka;

import java.util.UUID;

public class QuizCreatedEvent {
	
	private UUID quizId;
	private String creatorId;
	
	public QuizCreatedEvent(UUID quizId, String creatorId) {
		this.quizId = quizId;
		this.creatorId = creatorId;
	}
	
	public UUID getQuizId() {
		return quizId;
	}
	public void setQuizId(UUID quizId) {
		this.quizId = quizId;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	
	
	
}
