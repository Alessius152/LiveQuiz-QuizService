package com.quizservice.livequiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.quizservice.livequiz.config.FirebaseConfig;

@SpringBootApplication
public class LiveQuizQuizServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiveQuizQuizServiceApplication.class, args);
	}

}
