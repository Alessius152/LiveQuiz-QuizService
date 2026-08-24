package com.quizservice.livequiz.config;

import org.bson.UuidRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class DatabaseConfig {

	@Value("${spring.mongodb.uri}")
	private String dbUri;
	
	@Value("${spring.mongodb.database}")
	private String dbName;
	
    @Bean MongoClient mongoClient() {
    	MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(dbUri))
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build();
                
        return MongoClients.create(settings);
    }

    @Bean MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), dbName);
    }
	
}
