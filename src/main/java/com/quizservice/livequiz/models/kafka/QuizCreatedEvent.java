package com.quizservice.livequiz.models.kafka;

import java.util.ArrayList;
import java.util.UUID;

public class QuizCreatedEvent {
	
	private ArrayList<Object> quiz;
	private ArrayList<QuestionIndexing> indexing;

	public QuizCreatedEvent(UUID quizId, String creatorId, ArrayList<QuestionIndexing> questionsIndexing) {
		this.quiz = new ArrayList<Object>();
		this.quiz.add(creatorId);
		this.quiz.add(quizId);
		
		this.indexing = questionsIndexing;
	}
	
	public ArrayList<Object> getQuiz() {
		return quiz;
	}


	public void setQuiz(ArrayList<Object> quiz) {
		this.quiz = quiz;
	}

	public ArrayList<QuestionIndexing> getIndexing() {
		return indexing;
	}

	public void setIndexing(ArrayList<QuestionIndexing> qIndexing) {
		this.indexing = qIndexing;
	}
	
}
