package com.quizservice.livequiz.models.database;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

@Document(collection = "releases")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LastQuizReleaseDocumentModel {
	
	@Id
	private String id;
	
	private UUID quizId;
	private short lastRelease;
	
	public LastQuizReleaseDocumentModel(UUID quizId, short lastRelease) {
		this.quizId = quizId;
		this.lastRelease = lastRelease;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UUID getQuizId() {
		return quizId;
	}

	public void setQuizId(UUID quizId) {
		this.quizId = quizId;
	}

	public short getLastRelease() {
		return lastRelease;
	}

	public void setLastRelease(short lastRelease) {
		this.lastRelease = lastRelease;
	}
	
	
	
}
