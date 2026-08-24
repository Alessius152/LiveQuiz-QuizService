package com.quizservice.livequiz.models.httpRequests;

import com.fasterxml.jackson.annotation.JsonValue;

public enum QuestionType {
	TRUE_FALSE(0),
	OPEN_QUESTION(1),
	CLOSED_QUESTION(2);
	
	private final byte value;

	QuestionType(int value) {
        this.value = (byte) value;
    }
	
	@JsonValue
	public byte getValue() {
		return value;
	}
}
