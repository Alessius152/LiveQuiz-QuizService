package com.quizservice.livequiz.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.quizservice.livequiz.models.database.QuizDocumentModel;

public interface QuizRepository extends MongoRepository<QuizDocumentModel, String> {
	
	long deleteByCreatorIdAndQuizId(String creatorId, UUID quizId);

	Optional<QuizDocumentModel> findByCreatorIdAndQuizId(String creatorId, UUID quizId);
	
	@Query(
		value = "{ 'name': { $regex: ?0, $options: 'i' } }", 
	    fields = "{ 'name': 1, 'description': 1, 'creatorId': 1, 'quizId': 1, _id: 0 }"
	)
	Page<QuizDocumentModel> findByNameContainingIgnoreCase(String name, Pageable pageable);
	
}