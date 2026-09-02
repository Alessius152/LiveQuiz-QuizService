package com.quizservice.livequiz.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {
	
	@Bean FirebaseApp initializeApp() throws FileNotFoundException, SecurityException, IOException, IllegalStateException {
		ClassPathResource resource = new ClassPathResource("firebase-private-credentials.json");
		InputStream serviceAccount = resource.getInputStream();
		GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
		FirebaseOptions.Builder builder = FirebaseOptions.builder();

		if(FirebaseApp.getApps().isEmpty()) {
			FirebaseOptions options = builder.setCredentials(credentials).build();
			FirebaseApp app = FirebaseApp.initializeApp(options);
			return app;
		}
		else {
			return FirebaseApp.getInstance();
		} 
		
	}
	
}
