package com.quizservice.livequiz.QuizController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.quizservice.livequiz.controllers.QuizController;
import com.quizservice.livequiz.models.database.QuizDocumentModel;
import com.quizservice.livequiz.repositories.QuizRepository;

@WebMvcTest(QuizController.class)
public class CreateQuizTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private QuizRepository quizRepository;
	
	@Test
	@DisplayName("POST /quiz/create -> 201 Created (creazione quiz con domanda vero/falso)")
	void should_201_when_payload_valid_1() throws Exception {
		
		String payload = """
			{
				"name": "FirstQuiz_201",
				"questions": [
					{
						"question": "FirstQuestion",
						"type": 0,
						"answers": [1]
					}
				]
			}	
		""";
		
		when(
			quizRepository.insert(any(QuizDocumentModel.class))
		).thenAnswer(invocation -> invocation.getArgument(0));
		
		MockHttpServletRequestBuilder request = post("/quiz/create").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer TOKEN").content(payload);
		
		mockMvc.perform(request).andExpect(status().isCreated()).andExpect(jsonPath("$.quizId").exists());
		
	}
	
	@Test
	@DisplayName("POST /quiz/create -> 201 Created (creazione quiz con domanda a risposta aperta)")
	void should_201_when_payload_valid_2() throws Exception {
		
		String payload = """
			{
				"name": "FirstQuiz_201",
				"questions": [
					{
						"question": "FirstQuestion",
						"type": 1
					}
				]
			}	
		""";
		
		when(
			quizRepository.insert(any(QuizDocumentModel.class))
		).thenAnswer(invocation -> invocation.getArgument(0));
		
		MockHttpServletRequestBuilder request = post("/quiz/create").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer TOKEN").content(payload);
		
		mockMvc.perform(request).andExpect(status().isCreated()).andExpect(jsonPath("$.quizId").exists());
		
	}

	@Test
	@DisplayName("POST /quiz/create -> 201 Created (creazione quiz con domanda chiusa con diverse opzioni e risposte corrette)")
	void should_201_when_payload_valid_3() throws Exception {
		
		String payload = """
			{
				"name": "FirstQuiz_201",
				"questions": [
					{
						"question": "FirstQuestion",
						"type": 2,
						"answerables": [
							{
								"index": 10, 
								"text": "FirstOption"
							},
							{
								"index": 11, 
								"text": "SecondOption"
							},
							{
								"index": 20, 
								"text": "ThirdOption"
							},
							{
								"index": 21, 
								"text": "FourthOption"
							}
						],
						"answers": [11, 20]
					}
				]
			}	
		""";
		
		when(
			quizRepository.insert(any(QuizDocumentModel.class))
		).thenAnswer(invocation -> invocation.getArgument(0));
		
		MockHttpServletRequestBuilder request = post("/quiz/create").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer TOKEN").content(payload);
		
		mockMvc.perform(request).andExpect(status().isCreated()).andExpect(jsonPath("$.quizId").exists());
		
	}

	@Test
	@DisplayName("POST /quiz/create -> 400 Bad Request (domanda vero/falso senza risposta effettiva)")
	void should_400_when_payload_invalid_1() throws Exception {
		
		String payload = """
			{
				"name": "FirstQuiz_201",
				"questions": [
					{
						"question": "FirstQuestion",
						"type": 0
					}
				]
			}	
		""";
		
		when(
			quizRepository.insert(any(QuizDocumentModel.class))
		).thenAnswer(invocation -> invocation.getArgument(0));
		
		MockHttpServletRequestBuilder request = post("/quiz/create").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer TOKEN").content(payload);
		
		mockMvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("$.invalidQuestions").exists());
		
	}
	
	@Test
	@DisplayName("POST /quiz/create -> 400 Bad Request (domanda a risposta aperta con opzioni e risposta corretta)")
	void should_400_when_payload_invalid_2() throws Exception {
		
		String payload = """
			{
				"name": "FirstQuiz_201",
				"questions": [
					{
						"question": "FirstQuestion",
						"type": 1,
						"answerables": [
							{
								"index": 1, 
								"text": "FirstOption"
							},
							{
								"index": 2, 
								"text": "SecondOption"
							}
						],
						"answers": [1]
					}
				]
			}	
		""";
		
		when(
			quizRepository.insert(any(QuizDocumentModel.class))
		).thenAnswer(invocation -> invocation.getArgument(0));
		
		MockHttpServletRequestBuilder request = post("/quiz/create").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer TOKEN").content(payload);
		
		mockMvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("$.invalidQuestions").exists());
		
	}

	@Test
	@DisplayName("POST /quiz/create -> 400 Bad Request (creazione quiz con domanda chiusa con diverse opzioni e risposte corrette)")
	void should_400_when_payload_invalid_3() throws Exception {
		
		String payload = """
			{
				"name": "FirstQuiz_201",
				"questions": [
					{
						"question": "FirstQuestion",
						"type": 2
					}
				]
			}	
		""";
		
		when(
			quizRepository.insert(any(QuizDocumentModel.class))
		).thenAnswer(invocation -> invocation.getArgument(0));
		
		MockHttpServletRequestBuilder request = post("/quiz/create").contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer TOKEN").content(payload);
		
		mockMvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("$.invalidQuestions").exists());
		
	}
	
}
