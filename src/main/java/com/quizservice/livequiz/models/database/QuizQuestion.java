package com.quizservice.livequiz.models.database;

import java.util.ArrayList;

public class QuizQuestion {
	
	private String question;
	private byte type;
	private ArrayList<QuestionAnswerable> answerables;
	private ArrayList<Short> answers;

	public QuizQuestion() {	}
	
	public QuizQuestion(String question, byte type, ArrayList<QuestionAnswerable> answerables, ArrayList<Short> answers) {
		this.question = question;
		this.type = type;
		this.answerables = answerables;
		this.answers = answers;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
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
	
}
