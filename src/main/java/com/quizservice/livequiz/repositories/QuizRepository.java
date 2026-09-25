package com.quizservice.livequiz.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quizservice.livequiz.models.database.QuizDocumentModel;

public interface QuizRepository extends MongoRepository<QuizDocumentModel, String>, QuizRepositoryCustom {
	
	long deleteByCreatorIdAndQuizId(String creatorId, UUID quizId);

	Optional<QuizDocumentModel> findByCreatorIdAndQuizId(String creatorId, UUID quizId);
	
}