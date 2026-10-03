package com.quizservice.livequiz.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.quizservice.livequiz.errors.validation.InvalidQuizQuestionReason;
import com.quizservice.livequiz.logicValidations.QuizValidations;
import com.quizservice.livequiz.models.database.LastQuizReleaseDocumentModel;
import com.quizservice.livequiz.models.database.QuizDocumentModel;
import com.quizservice.livequiz.models.database.summaries.FetchQuizzesListSummaryModel;
import com.quizservice.livequiz.models.httpRequests.CreateQuizRequestModel;
import com.quizservice.livequiz.models.httpRequests.QuizQuestion;
import com.quizservice.livequiz.models.kafka.QuizCreatedEventMaker;
import com.quizservice.livequiz.models.kafka.QuizNewVersionAvailableEvent;
import com.quizservice.livequiz.models.kafka.QuizNewVersionReleasedEventMaker;
import com.quizservice.livequiz.repositories.LastReleasesRepository;
import com.quizservice.livequiz.repositories.QuizRepository;

//TODO in un quiz, per ora, tutte le domande devono avere index univoco, ma le answerables possono avere index doppione, e questo è un BUG

@Service
public class QuizService {

	private QuizRepository quizRepository;
	private LastReleasesRepository lastReleasesRepository;
	private final KafkaQuizProducerService producerService;
	
	public QuizService(QuizRepository quizRepository, LastReleasesRepository lastReleasesRepository, KafkaQuizProducerService producerService) {
		this.quizRepository = quizRepository;
		this.lastReleasesRepository = lastReleasesRepository;
		this.producerService = producerService;
	}
	
	public ResponseEntity<Map<String, Object>> createQuiz(String userId, CreateQuizRequestModel requestBody) {
		ArrayList<QuizQuestion> questions = requestBody.getQuestions();
		HashMap<String, ArrayList<InvalidQuizQuestionReason>> invalidQuestions = new HashMap<String, ArrayList<InvalidQuizQuestionReason>>();
				
		if(questions != null) {
			questions.sort(Comparator.comparing(QuizQuestion::getIndex));
			
			if(questions.get(0).getIndex() != 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
					"app_error", "first_question_index_must_be_zero"
				));
			}
			
			boolean hasDuplicates = questions.stream().map(QuizQuestion::getIndex).distinct().count() < questions.size();
			
			if(hasDuplicates) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
					"app_error", "each_question_index_must_be_unique"
				));
			}
			
			invalidQuestions = QuizValidations.validateQuestions(questions);
				
			if(invalidQuestions.size() > 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
					"app_error", "there_are_invalid_questions",
					"invalidQuestions", invalidQuestions)
				);
			}
				
			HashMap<String, ArrayList<Short>> unanswerables = QuizValidations.getUnanswerables(questions);
			
			if(unanswerables.size() > 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
					"app_error", "there_are_unanswerables",
					"unanswerables", unanswerables)
				);
			}
		}
		
		final short version = 0;
		final UUID quizId = UUID.randomUUID();
		
		LastQuizReleaseDocumentModel firstReleaseModel = new LastQuizReleaseDocumentModel(quizId, version);
		QuizDocumentModel model = new QuizDocumentModel(version, quizId, requestBody.getName(), requestBody.getDescription(), userId, questions);
		
		lastReleasesRepository.insert(firstReleaseModel);
		quizRepository.insert(model);
		producerService.sendQuizCreated(QuizCreatedEventMaker.make(model));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("quizId", model.getQuizId()));
	}
	
	public ResponseEntity<Map<String, Object>> deleteQuiz(String userId, UUID quizId) {
		
		Long deletedCount = quizRepository.deleteByCreatorIdAndQuizId(userId, quizId);
		
		if(deletedCount == 0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of("deletedCount", deletedCount));
		
	}
	
	public ResponseEntity<Map<String, Object>> addQuestions(String userId, UUID quizId, ArrayList<QuizQuestion> questions) {
		
		HashMap<String, ArrayList<InvalidQuizQuestionReason>> invalidQuestions = QuizValidations.validateQuestions(questions);
		if(invalidQuestions.size() > 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				Map.of(
					"app_error", "there_are_invalid_questions",
					"invalidQuestions", invalidQuestions
				)
			);
		}
			
		HashMap<String, ArrayList<Short>> unanswerables = QuizValidations.getUnanswerables(questions);
		if(unanswerables.size() > 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
				"app_error", "there_are_unanswerables",
				"unanswerables", unanswerables)
			);
		}
		
		questions.sort(Comparator.comparing(QuizQuestion::getIndex));

		Optional<LastQuizReleaseDocumentModel> quizReleaseOpt = lastReleasesRepository.findByQuizId(quizId);
		if(quizReleaseOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		LastQuizReleaseDocumentModel quizReleaseModel = quizReleaseOpt.get();
		
		Optional<QuizDocumentModel> quizOpt = quizRepository.findByCreatorIdAndQuizIdAndVersion(userId, quizId, quizReleaseOpt.get().getLastRelease());
		if(quizOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		
		QuizDocumentModel quizModel = quizOpt.get();
		ArrayList<QuizQuestion> quizQuestions = quizModel.getQuestions();
		
		if((quizQuestions == null) || (quizQuestions.isEmpty())) {
			if(questions.get(0).getIndex() != 0) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
					"app_error", "first_question_index_must_be_zero"
				));
			}
		}
		
		int dbQuestionsCount = (quizQuestions != null) ? quizQuestions.size() : 0;
		int payloadQuestionsCount = questions.size();
		int expectedSize = dbQuestionsCount + payloadQuestionsCount; /*una volta dichiarato 
		- quante domande ci sono già (esempio: 3)
		- quante ne voglio aggiungere (esempio: 3)
		mi aspetto che il merge di queste due quantità sia 6, perché , se con lo stream.concat
		che controllo in seguito ottengo un numero minore, vuol dire che ci sono dei doppioni,
		quindi
		(aggiungo una o più domande nel payload con indice pari) 
			OPPURE 
		(specifico nel payload domande con indici già presenti nei dati di quel quiz)
		*/
		
		Stream<Short> payloadQuestionIndexes = questions.stream().map(QuizQuestion::getIndex);
		Stream<Short> dbQuestionIndexes = (quizQuestions != null) ? quizQuestions.stream().map(QuizQuestion::getIndex) : Stream.empty();
		
		if(
			Stream.concat(payloadQuestionIndexes, dbQuestionIndexes).distinct().count()
			< expectedSize
		) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
				"app_error", "each_question_index_must_be_unique"
			));
		}
		
		int currentSize = (quizQuestions == null) ? 0 : quizQuestions.size();
		if((currentSize + questions.size()) > 150) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(
				Map.of(
					"app_error", "quiz_questions_count_maximum_reached",
					"maximum_addable", (150 - currentSize),
					"you_specified", questions.size(),
					"current_questions_count", currentSize
				)
			);
		}

		short newVersion = (short) (quizReleaseModel.getLastRelease() + 1);
		
		ArrayList<QuizQuestion> mergedQuestions = new ArrayList<QuizQuestion>();
		
		if ((quizModel.getQuestions() != null) && (! quizModel.getQuestions().isEmpty())) {
			mergedQuestions.addAll(quizModel.getQuestions());
		}
		
		mergedQuestions.addAll(questions);
		mergedQuestions.sort(Comparator.comparing(QuizQuestion::getIndex));
		
		long hasUpdatedVersion = lastReleasesRepository.updateByLastRelease(quizId, quizReleaseModel.getLastRelease(), newVersion);
		
		if(hasUpdatedVersion != 1) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		
		QuizDocumentModel model = new QuizDocumentModel(newVersion, quizModel.getQuizId(), quizModel.getName(), quizModel.getDescription(), userId, mergedQuestions);
		quizRepository.insert(model);
		producerService.sendQuizHasNewVersion(QuizNewVersionReleasedEventMaker.make(quizModel.getQuizId(), newVersion, questions));

		return ResponseEntity.status(HttpStatus.OK).body(null);
		
	}
	
	public Page<FetchQuizzesListSummaryModel> searchQuizzes(String name, Pageable pageable) {
		return quizRepository.findByNameContainingIgnoreCase(name, pageable);
	}
	
	public ResponseEntity<Object> getQuiz(UUID quizId) {
		return ResponseEntity.ok().body(quizRepository.getQuiz(quizId));
	}
	
}
