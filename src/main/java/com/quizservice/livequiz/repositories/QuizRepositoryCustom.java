package com.quizservice.livequiz.repositories;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mongodb.client.result.UpdateResult;
import com.quizservice.livequiz.models.database.summaries.FetchQuizSummaryModel;
import com.quizservice.livequiz.models.database.summaries.FetchQuizzesListSummaryModel;
import com.quizservice.livequiz.models.httpRequests.QuizQuestion;

public interface QuizRepositoryCustom {
	UpdateResult pushQuestions (String creatorId, UUID quizId, ArrayList<QuizQuestion> questions);
	public FetchQuizSummaryModel getQuiz(UUID quizId);

    Page<FetchQuizzesListSummaryModel> findByNameContainingIgnoreCase(String name, Pageable pageable);
	
}