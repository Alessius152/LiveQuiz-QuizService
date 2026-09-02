package com.quizservice.livequiz.config;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

@Configuration
public class KafkaProducerConfig {
	
	@Bean ProducerFactory<String, Object> producerFactory() {
		return new DefaultKafkaProducerFactory<>(Map.of(
			ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "livequiz_kafka:9092",
			ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
			ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class,
			ProducerConfig.RETRIES_CONFIG, 3,
			ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 5000
		));
	}
	
	@Bean KafkaTemplate<String, Object> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
	
}
