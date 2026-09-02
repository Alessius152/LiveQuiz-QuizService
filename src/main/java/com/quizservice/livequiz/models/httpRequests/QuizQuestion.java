package com.quizservice.livequiz.models.httpRequests;

import java.util.ArrayList;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class QuizQuestion {
	
	@NotNull
	private short index;
	
	@NotBlank(message = "Any question you add must have a text and at least 2 possible answers")
	@Size(min = 1, max = 1024, message = "Any question's title must have a length between 1 and 1024 characters")
	private String question;
	
	private QuestionType type;
	
	@Size(min = 2, max = 18, message = "Question's answers list must be between 2 and 18 possible options")
	private ArrayList<QuestionAnswerable> answerables;

	@Size(min = 1, max = 18, message = "The answers list must contain between 1 and 18 options")
	private ArrayList<Short> answers;
	
	public QuizQuestion() {	}
	
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public QuestionType getType() {
		return type;
	}

	public void setType(QuestionType type) {
		this.type = type;
	}

	public ArrayList<QuestionAnswerable> getAnswerables() {
		return answerables;
	}

	public void setAnswerables(ArrayList<QuestionAnswerable> answerables) {
		this.answerables = answerables;
	}

	public ArrayList<Short> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<Short> answers) {
		this.answers = answers;
	}
	
	public short getIndex() {
		return index;
	}

	public void setIndex(short index) {
		this.index = index;
	}
	
}