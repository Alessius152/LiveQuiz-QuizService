package com.quizservice.livequiz.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quizservice.livequiz.errors.validation.InvalidQuizQuestionReason;
import com.quizservice.livequiz.logicValidations.QuizValidations;
import com.quizservice.livequiz.models.database.QuizDocumentModel;
import com.quizservice.livequiz.models.httpRequests.CreateQuizRequestModel;
import com.quizservice.livequiz.models.httpRequests.QuizQuestion;
import com.quizservice.livequiz.repositories.QuizRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("/quiz")
public class QuizController {
	
	private final QuizRepository quizRepository;
	private ObjectMapper objectMapper;
	
	public QuizController(QuizRepository quizRepository, ObjectMapper objectMapper) {
		this.quizRepository = quizRepository;
		this.objectMapper = objectMapper;
	}
	
	@PostMapping("/create")
	public ResponseEntity<ObjectNode> createQuiz(
		HttpServletRequest request,
		@Valid @RequestBody CreateQuizRequestModel requestBody
	) {	
		//FirebaseToken token = (FirebaseToken) request.getAttribute("firebaseProfile");
		ArrayList<QuizQuestion> questions = requestBody.getQuestions();
		HashMap<String, ArrayList<InvalidQuizQuestionReason>> invalidQuestions = new HashMap<String, ArrayList<InvalidQuizQuestionReason>>();
		
		if(questions != null) {
			invalidQuestions = QuizValidations.validateQuestions(questions);
			
			if(invalidQuestions.size() > 0) {
				ObjectNode invalidQuestionsNode = objectMapper.createObjectNode();
				invalidQuestionsNode.putPOJO("invalidQuestions", invalidQuestions);
				return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(invalidQuestionsNode);
			}
			
			HashMap<String, ArrayList<Short>> unanswerables = QuizValidations.getUnanswerables(questions);
			
			if(unanswerables.size() > 0) {
				ObjectNode unanswerablesErrorNode = objectMapper.createObjectNode();
				unanswerablesErrorNode.putPOJO("unanswerables", unanswerables);
				return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(unanswerablesErrorNode);
			}
		}
		
		QuizDocumentModel model = new QuizDocumentModel(requestBody.getName(), requestBody.getDescription(), "token.getUid()", questions);
		quizRepository.insert(model);

		ObjectNode responseNode = objectMapper.createObjectNode();
		responseNode.putPOJO("quizId", model.getQuizId());
		
		return ResponseEntity.status(HttpStatus.SC_CREATED).body(responseNode);
	}
	
}
