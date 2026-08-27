package com.quizservice.livequiz.models.httpRequests;

import java.util.ArrayList;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AddQuestionsRequestModel {
	
	@NotNull(message = "When adding questions, you cannot omit the questions field")
	@Valid
	@Size(min = 1, max = 150, message = "Questions field length must be between 1 and 150")
	private ArrayList<QuizQuestion> questions;

	public AddQuestionsRequestModel() {}
	
	public ArrayList<QuizQuestion> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<QuizQuestion> questions) {
		this.questions = questions;
	}
	
}
