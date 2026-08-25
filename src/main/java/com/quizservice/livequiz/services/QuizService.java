package com.quizservice.livequiz.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.quizservice.livequiz.errors.validation.InvalidQuizQuestionReason;
import com.quizservice.livequiz.logicValidations.QuizValidations;
import com.quizservice.livequiz.models.database.QuizDocumentModel;
import com.quizservice.livequiz.models.httpRequests.CreateQuizRequestModel;
import com.quizservice.livequiz.models.httpRequests.QuizQuestion;
import com.quizservice.livequiz.repositories.QuizRepository;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

@Service
public class QuizService {
	
	private QuizRepository quizRepository;
	private ObjectMapper objectMapper;
	
	public QuizService(QuizRepository quizRepository, ObjectMapper objectMapper) {
		this.quizRepository = quizRepository;
		this.objectMapper = objectMapper;
	}
	
	public ResponseEntity<ObjectNode> createQuiz(String userId, CreateQuizRequestModel requestBody) {
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
				
		QuizDocumentModel model = new QuizDocumentModel(requestBody.getName(), requestBody.getDescription(), userId, questions);
		quizRepository.insert(model);

		ObjectNode responseNode = objectMapper.createObjectNode();
		responseNode.putPOJO("quizId", model.getQuizId());
		
		return ResponseEntity.status(HttpStatus.SC_CREATED).body(responseNode);
	}
	

	public ResponseEntity<ObjectNode> deleteQuiz(String userId, UUID quizId) {
		
		Long deletedCount = quizRepository.deleteByCreatorIdAndQuizId(userId, quizId);
		
		if(deletedCount == 0) {
			return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
		}
		
		ObjectNode deletedNode = objectMapper.createObjectNode();
		deletedNode.put("deletedCount", deletedCount);
		
		return ResponseEntity.status(HttpStatus.SC_OK).body(deletedNode);
		
	}
}
