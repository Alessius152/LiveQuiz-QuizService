package com.quizservice.livequiz.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import com.quizservice.livequiz.models.database.LastQuizReleaseDocumentModel;
import com.quizservice.livequiz.models.database.QuizDocumentModel;

public interface LastReleasesRepository extends MongoRepository<LastQuizReleaseDocumentModel, String> {
	Optional<LastQuizReleaseDocumentModel> findByQuizId(UUID quizId);
	
    @Query("{ 'quizId': ?0, 'lastRelease': ?1 }")
    @Update("{ '$set': { 'lastRelease': ?2 } }")
    long updateByLastRelease(UUID quizId, short currentVersion, short newVersion);
	
}
