package com.quizservice.livequiz.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {
	
	@Bean FirebaseApp initializeApp() throws FileNotFoundException, SecurityException, IOException, IllegalStateException {
		FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase-private-credentials.json");
		GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
		FirebaseOptions.Builder builder = FirebaseOptions.builder();
		FirebaseOptions options = builder.setCredentials(credentials).build();
		FirebaseApp app = FirebaseApp.initializeApp(options);
		return app;
	}
	
}
