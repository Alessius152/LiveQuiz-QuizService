package com.quizservice.livequiz.models.httpRequests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateQuizRequestModel {

	@NotBlank(message = "Quiz must have a name")
	@Size(max = 200, message = "Quiz name must be under 200 chars")
	private String name;
	
	@Size(max = 800, message = "Quiz description must be under 800 chars")
	private String description;
	
	public CreateQuizRequestModel() {}
	
	public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
	
}
