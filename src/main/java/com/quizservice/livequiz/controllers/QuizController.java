package com.quizservice.livequiz.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quizservice.livequiz.models.httpRequests.CreateQuizRequestModel;
import com.quizservice.livequiz.services.QuizService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("/quiz")
public class QuizController {
	
	private final QuizService quizService;
	
	public QuizController(QuizService quizService, ObjectMapper objectMapper) {
		this.quizService = quizService;
	}
	
	@PostMapping("/create")
	public ResponseEntity<ObjectNode> createQuiz(HttpServletRequest request,
		@Valid @RequestBody CreateQuizRequestModel requestBody
	) {	
		//FirebaseToken token = (FirebaseToken) request.getAttribute("firebaseProfile");
		return quizService.createQuiz("token.getUid()", requestBody);
	}
	
	@DeleteMapping("/delete/{quizId}")
	public ResponseEntity<ObjectNode> deleteQuiz(@PathVariable("quizId") UUID quizId) {
		//FirebaseToken token = (FirebaseToken) request.getAttribute("firebaseProfile");
		return quizService.deleteQuiz("token.getUid()", quizId);
	}
	
}
