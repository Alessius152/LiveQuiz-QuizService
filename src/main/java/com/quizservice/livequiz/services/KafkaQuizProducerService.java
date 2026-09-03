package com.quizservice.livequiz.services;

import java.nio.charset.StandardCharsets;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.quizservice.livequiz.models.kafka.QuizCreatedEvent;

@Service
public class KafkaQuizProducerService {

    private static final String TOPIC = "quiz-topic";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaQuizProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendQuizCreated(QuizCreatedEvent eventData) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(TOPIC, eventData);
        record.headers().add("eventName", "QuizCreatedEvent".getBytes(StandardCharsets.US_ASCII));
        kafkaTemplate.send(record);
    }
}