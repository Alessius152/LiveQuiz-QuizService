package com.quizservice.livequiz.controllers;

import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.FirebaseToken;
import com.quizservice.livequiz.models.database.QuizDocumentModel;
import com.quizservice.livequiz.models.httpRequests.AddQuestionsRequestModel;
import com.quizservice.livequiz.models.httpRequests.CreateQuizRequestModel;
import com.quizservice.livequiz.models.httpRequests.DeleteQuestionsRequestModel;
import com.quizservice.livequiz.services.QuizService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/quiz")
public class QuizController {
	
	private final QuizService quizService;
	
	public QuizController(QuizService quizService, ObjectMapper objectMapper) {
		this.quizService = quizService;
	}
	
	@PostMapping("/create")
	public ResponseEntity<Map<String, Object>> createQuiz(HttpServletRequest request,
		@Valid @RequestBody CreateQuizRequestModel requestBody
	) {	
		//FirebaseToken token = (FirebaseToken) request.getAttribute("firebaseProfile");
		return quizService.createQuiz("token.getUid()", requestBody);
	}
	
	@DeleteMapping("/delete/{quizId}")
	public ResponseEntity<Map<String, Object>> deleteQuiz(HttpServletRequest request, @PathVariable("quizId") UUID quizId) {
		//FirebaseToken token = (FirebaseToken) request.getAttribute("firebaseProfile");
		return quizService.deleteQuiz("token.getUid()", quizId);
	}
	
	@PutMapping("/addQuestions/{quizId}")
	public ResponseEntity<Map<String, Object>> addQuestions(
		HttpServletRequest request, 
		@PathVariable("quizId") UUID quizId,
		@Valid @RequestBody AddQuestionsRequestModel requestBody
	) {
		//FirebaseToken token = (FirebaseToken) request.getAttribute("firebaseProfile");
		return quizService.addQuestions("token.getUid()", quizId, requestBody.getQuestions());
	}
	
	@GetMapping("/{quizTitle}")
	public ResponseEntity<Map<String, Object>> fetchQuizzes(
		@PathVariable("quizTitle") String quizTitle,
		Pageable pageable
	){
		Pageable fixedPageable = PageRequest.of(pageable.getPageNumber(), 20, pageable.getSort());
		Page<QuizDocumentModel> quizzes = quizService.searchQuizzes(quizTitle, fixedPageable);
		
		return ResponseEntity.status(HttpStatus.SC_OK).body(Map.of("quizzesPage", quizzes));
	}
	
	@PostMapping("/removeQuestions/{quizId}")
	public ResponseEntity<Map<String, Object>> removeQuesstions(
		@PathVariable("quizId") String quizId,
		HttpServletRequest request,
		@Valid @RequestBody DeleteQuestionsRequestModel requestBody
	) {
		//FirebaseToken token = (FirebaseToken) request.getAttribute("firebaseProfile");
		return ResponseEntity.status(200).body(null);
	}
	
}
