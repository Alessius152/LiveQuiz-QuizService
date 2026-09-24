package com.quizservice.livequiz.interceptors.http;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {
	
	public static String JWT_PREFIX = "Bearer ";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception, IndexOutOfBoundsException {
		
		String authorization = request.getHeader("Authorization");
		
		if(
			(authorization == null)
			|| (!authorization.startsWith(JWT_PREFIX))
		) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "missing authorization header");
			return false;
		}
		
		String token = authorization.substring(JWT_PREFIX.length());
		
		try {
			FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
			request.setAttribute("firebaseProfile", decodedToken);
			return true;
		}
		catch(Exception e) {
			if(e instanceof FirebaseAuthException) {
				writeErrorJson(request, response, (FirebaseAuthException)e);
				return false;
			}
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "invalid firebase bearer token");
			return false;
		}
		
	}
	
	private void writeErrorJson(HttpServletRequest request, HttpServletResponse response, FirebaseAuthException e) throws java.io.IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		Map<String, Object> errorBody = new LinkedHashMap<>();
		errorBody.put("timestamp", Instant.now().toString());
		errorBody.put("status", 401);
		errorBody.put("error", e.getErrorCode()); 
		errorBody.put("path", request.getRequestURI());
		
		ObjectMapper objectMapper = new ObjectMapper();
		response.getWriter().write(objectMapper.writeValueAsString(errorBody));
	}
	
}
