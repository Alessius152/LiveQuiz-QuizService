package com.quizservice.livequiz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.quizservice.livequiz.interceptors.http.BearerAuthInterceptor;

@Configuration
public class BearerAuthConfig implements WebMvcConfigurer {
	
	private final BearerAuthInterceptor bearerAuthInterceptor;

	BearerAuthConfig(BearerAuthInterceptor bearerAuthInterceptor) {
		this.bearerAuthInterceptor = bearerAuthInterceptor;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/quiz/**");
	}
	
}
