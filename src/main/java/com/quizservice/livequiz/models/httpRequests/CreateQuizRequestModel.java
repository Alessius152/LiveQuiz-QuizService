package com.quizservice.livequiz.models.httpRequests;

import java.util.ArrayList;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateQuizRequestModel {

	@NotBlank(message = "Quiz must have a name")
	@Size(max = 200, message = "Quiz name must be under 200 chars")
	private String name;
	
	@Size(max = 800, message = "Quiz description must be under 800 chars")
	private String description;
	
	@Valid
	@Size(min = 1, max = 150, message = "If you want to specify some questions, you must specify at least one")
	private ArrayList<QuizQuestion> questions;
	
	public CreateQuizRequestModel() {}
	
	public CreateQuizRequestModel(String name, String description, ArrayList<QuizQuestion> questions) {
		this.name = name;
		this.description = description;
		this.questions = questions;
	}
	
	public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

	public ArrayList<QuizQuestion> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<QuizQuestion> questions) {
		this.questions = questions;
	}
	
}
