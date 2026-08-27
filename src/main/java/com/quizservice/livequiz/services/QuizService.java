package com.quizservice.livequiz.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.quizservice.livequiz.errors.validation.InvalidQuizQuestionReason;
import com.quizservice.livequiz.logicValidations.QuizValidations;
import com.quizservice.livequiz.models.database.QuizDocumentModel;
import com.quizservice.livequiz.models.httpRequests.CreateQuizRequestModel;
import com.quizservice.livequiz.models.httpRequests.QuizQuestion;
import com.quizservice.livequiz.repositories.QuizRepository;

@Service
public class QuizService {
	
	private QuizRepository quizRepository;
	
	public QuizService(QuizRepository quizRepository) {
		this.quizRepository = quizRepository;
	}
	
	public ResponseEntity<Map<String, Object>> createQuiz(String userId, CreateQuizRequestModel requestBody) {
		ArrayList<QuizQuestion> questions = requestBody.getQuestions();
		HashMap<String, ArrayList<InvalidQuizQuestionReason>> invalidQuestions = new HashMap<String, ArrayList<InvalidQuizQuestionReason>>();
				
		if(questions != null) {
			invalidQuestions = QuizValidations.validateQuestions(questions);
				
			if(invalidQuestions.size() > 0) {
				return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(Map.of(
					"app_error", "there_are_invalid_questions",
					"invalidQuestions", invalidQuestions)
				);
			}
				
			HashMap<String, ArrayList<Short>> unanswerables = QuizValidations.getUnanswerables(questions);
			
			if(unanswerables.size() > 0) {
				return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(Map.of(
					"app_error", "there_are_unanswerables",
					"unanswerables", unanswerables)
				);
			}
		}
				
		QuizDocumentModel model = new QuizDocumentModel(requestBody.getName(), requestBody.getDescription(), userId, questions);
		quizRepository.insert(model);
		
		return ResponseEntity.status(HttpStatus.SC_CREATED).body(Map.of("quizId", model.getQuizId()));
	}
	
	public ResponseEntity<Map<String, Object>> deleteQuiz(String userId, UUID quizId) {
		
		Long deletedCount = quizRepository.deleteByCreatorIdAndQuizId(userId, quizId);
		
		if(deletedCount == 0) {
			return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
		}
		
		return ResponseEntity.status(HttpStatus.SC_OK).body(Map.of("deletedCount", deletedCount));
		
	}
	
	public ResponseEntity<Map<String, Object>> addQuestions(String userId, UUID quizId, ArrayList<QuizQuestion> questions) {
		
		Optional<QuizDocumentModel> quizOpt = quizRepository.findByCreatorIdAndQuizId(userId, quizId);
		
		if(quizOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
		}
		
		QuizDocumentModel quizModel = quizOpt.get();
		ArrayList<QuizQuestion> quizQuestions = quizModel.getQuestions();
		
		if(quizQuestions == null) {
			quizModel.setQuestions(questions);
		}
		else {
			if((quizQuestions.size() + questions.size()) > 150) {
				return ResponseEntity.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).body(
					Map.of("app_error","quiz_questions_count_maximum_reached")
				);
			}
			
			//TODO: Valida anche la validazione logico-applicativa delle domande da inserire, come hai fatto per l'api createQuiz
			
			quizModel.getQuestions().addAll(questions);
		}
		
		quizRepository.save(quizModel);
		
		return ResponseEntity.status(HttpStatus.SC_OK).body(null);
		
	}
	
	public Page<QuizDocumentModel> searchQuizzes(String name, Pageable pageable) {
		return quizRepository.findByNameContainingIgnoreCase(name, pageable);
	}
	
}
