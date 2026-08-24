package com.quizservice.livequiz.models.httpRequests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class QuestionAnswerable {
	
	@NotNull
	private Short index;
	
	@NotNull
	@Size(min = 1, max = 512, message = "A single answer of a question must have a length between 1 and 512 characters")
	private String text;

	public QuestionAnswerable() {}
	
	public Short getIndex() {
		return index;
	}

	public void setIndex(Short index) {
		this.index = index;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
