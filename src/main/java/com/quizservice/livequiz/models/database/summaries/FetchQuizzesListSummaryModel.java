package com.quizservice.livequiz.models.database.summaries;

import java.util.UUID;

public class FetchQuizzesListSummaryModel {
    private UUID quizId;
    private String name;
    private String description;
    private String creatorId;
    private int questionCount;
    private int quizTime;
    
	public UUID getQuizId() {
		return quizId;
	}
	public void setQuizId(UUID quizId) {
		this.quizId = quizId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public int getQuestionCount() {
		return questionCount;
	}
	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}
	public int getQuizTime() {
		return quizTime;
	}
	public void setQuizTime(int quizTime) {
		this.quizTime = quizTime;
	}
}
