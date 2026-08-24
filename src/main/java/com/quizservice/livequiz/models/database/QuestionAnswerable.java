package com.quizservice.livequiz.models.database;

public class QuestionAnswerable {

	private Short index;
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
