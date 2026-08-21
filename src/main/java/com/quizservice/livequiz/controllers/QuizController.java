package com.quizservice.livequiz.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quizservice.livequiz.models.httpRequests.CreateQuizRequestModel;

import jakarta.validation.Valid;

@RestController
@RequestMapping("quiz")
public class QuizController {
	
	@PostMapping("create")
	public String createQuiz(
		@Valid @RequestBody CreateQuizRequestModel requestBody
	) {
		return requestBody.toString();
	}
	
}
