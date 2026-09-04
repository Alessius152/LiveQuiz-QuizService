package com.quizservice.livequiz.models.database.summaries;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "quizzes")
public class FetchQuizSummaryModel {
	
	private String name;
	private String description;
	private String creatorId;
	private ArrayList<Map<String, Object>> questions;
	
	public FetchQuizSummaryModel(String name, String description, String creatorId, ArrayList<Map<String, Object>> questions) {
		this.name = name;
		this.description = description;
		this.creatorId = creatorId;
		this.questions = questions;
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

	public ArrayList<Map<String, Object>> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<Map<String, Object>> questions) {
		this.questions = questions;
	}
	
}
