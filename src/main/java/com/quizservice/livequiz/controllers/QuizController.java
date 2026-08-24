package com.quizservice.livequiz.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.FirebaseToken;
import com.quizservice.livequiz.errors.validation.InvalidQuizQuestionError;
import com.quizservice.livequiz.logicValidations.QuizValidations;
import com.quizservice.livequiz.models.database.QuizDocumentModel;
import com.quizservice.livequiz.models.httpRequests.CreateQuizRequestModel;
import com.quizservice.livequiz.models.httpRequests.QuizQuestion;
import com.quizservice.livequiz.repositories.QuizRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/quiz")
public class QuizController {
	
	private final QuizRepository quizRepository;
	
	public QuizController(QuizRepository quizRepository) {
		this.quizRepository = quizRepository;
	}
	
	@PostMapping("/create")
	public ResponseEntity<HashMap<String, Object>> createQuiz(
		HttpServletRequest request,
		@Valid @RequestBody CreateQuizRequestModel requestBody
	) {	
		//FirebaseToken token = (FirebaseToken) request.getAttribute("firebaseProfile");
		ArrayList<QuizQuestion> questions = requestBody.getQuestions();
		HashMap<String, Object> response = new HashMap<String, Object>();
		
		if(questions != null) {
			ArrayList<InvalidQuizQuestionError> invalidQuestions = QuizValidations.validateQuestions(questions);
			if(invalidQuestions.size() > 0) {
				for(InvalidQuizQuestionError error : invalidQuestions) {
					response.put(String.valueOf(error.getqIndex()), error.getReasons());
				}
				return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(response);
			}
		}
		
		QuizDocumentModel model = new QuizDocumentModel(requestBody.getName(), requestBody.getDescription(), "FIREBASE_UID", questions);
		quizRepository.insert(model);
		
		response.put("quizId", model.getQuizId());
		
		return ResponseEntity.status(HttpStatus.SC_CREATED).body(response);
	}
	
}
