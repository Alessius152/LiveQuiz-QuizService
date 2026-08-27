
package com.quizservice.livequiz.models.database;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.quizservice.livequiz.models.httpRequests.QuizQuestion;

@Document(collection = "quizzes")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuizDocumentModel {
    
    @Id
    private String id; 
    
    @TextIndexed
    private String name;
    
    private String description;
    private String creatorId;
    private ArrayList<QuizQuestion> questions;
 
    private UUID quizId = UUID.randomUUID();

	public QuizDocumentModel() {}
    
    public QuizDocumentModel(String name, String description, String creatorId, ArrayList<QuizQuestion> questions) {
    	this.name = name;
    	
    	if((description == null) || (description.trim().isEmpty())) {
    		this.description = null;
    	}
    	else {
    		this.description = description;
    	}
    	
    	this.creatorId = creatorId;
    	this.questions = questions;
    }
    
    public ArrayList<QuizQuestion> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<QuizQuestion> questions) {
		this.questions = questions;
	}
    
	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public UUID getQuizId() {
		return quizId;
	}

	public void setQuizId(UUID quizId) {
		this.quizId = quizId;
	}
}