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
import com.quizservice.livequiz.models.database.summaries.FetchQuizzesListSummaryModel;
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
	
	/*@DeleteMapping("/delete/{quizId}")
	public ResponseEntity<Map<String, Object>> deleteQuiz(HttpServletRequest request, @PathVariable("quizId") UUID quizId) {
		//FirebaseToken token = (FirebaseToken) request.getAttribute("firebaseProfile");
		return quizService.deleteQuiz("token.getUid()", quizId);
	}*/ /*adesso che gestisco le versioni dei quiz, la situazione riguardo questa API diventa più complicata di quello che era.
	Innanzitutto devo eliminare tutte le versioni, ma di base, come anche prima, se un client richiede una versione del quiz
	e il quiz non c'è? succede che riceve un 404 e non può renderizzare la stanza, anche se può entrarci.
	
	Di base bisognerebbe cancellare in gruppo le versioni del quiz, quando viene richiesto dal creatore di esso, solo quando
	la Garbage Collection del Realtime Service ci dice tramite kafka che quel quiz non è in uso da nessuna stanza.
	
		Per "Garbage Collection del Realtime Service" si intende, tipicamente, un software che a ogni termine partita controlla
		se quel quiz, con il termine di quella partita, ha chiuso il suo ciclo di vita di utilizzo.
		
		Perché se ci sono 10 stanze che usano lo stesso quiz, solo al termine della decima si attiverà la Garbage Collection,
		ma per tutte e 10 verrà eseguito il controllo.
		
		Entrando un pò nello specifico del realtime service, per la copia mutabile dell'entità quiz:{uuid} ci sarà un contatore
		che incrementerà e decrementerà a seconda di quante stanze vengono inizializzate/finalizzate con, come immutableQuizSnapshot.quizId
		quello stesso quiz.
	*/
	
	@PutMapping("/addQuestions/{quizId}")
	public ResponseEntity<Map<String, Object>> addQuestions(
		HttpServletRequest request, 
		@PathVariable("quizId") UUID quizId,
		@Valid @RequestBody AddQuestionsRequestModel requestBody
	) {
		//FirebaseToken token = (FirebaseToken) request.getAttribute("firebaseProfile");
		return quizService.addQuestions("token.getUid()", quizId, requestBody.getQuestions());
	}
	
	@GetMapping("search/{quizTitle}")
	public ResponseEntity<Map<String, Object>> fetchQuizzes(
		@PathVariable("quizTitle") String quizTitle,
		Pageable pageable
	){
		Pageable fixedPageable = PageRequest.of(pageable.getPageNumber(), 20, pageable.getSort());
		Page<FetchQuizzesListSummaryModel> quizzes = quizService.searchQuizzes(quizTitle, fixedPageable);
		
		return ResponseEntity.status(HttpStatus.SC_OK).body(Map.of("quizzesPage", quizzes));
	}
	
	@GetMapping("/single/{quizId}")
	public ResponseEntity<Object> fetchQuiz(@PathVariable("quizId") UUID quizId) {
		return quizService.getQuiz(quizId);
	}
	
	@PostMapping("/removeQuestions/{quizId}")
	public ResponseEntity<Map<String, Object>> removeQuesstions(
		@PathVariable("quizId") String quizId,
		HttpServletRequest request,
		@Valid @RequestBody DeleteQuestionsRequestModel requestBody
	) {
		FirebaseToken token = (FirebaseToken) request.getAttribute("firebaseProfile");
		return ResponseEntity.status(200).body(null);
	}
	
}
