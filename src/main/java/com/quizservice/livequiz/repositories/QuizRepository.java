package com.quizservice.livequiz.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quizservice.livequiz.models.database.QuizDocumentModel;

public interface QuizRepository extends MongoRepository<QuizDocumentModel, String> {
    
}