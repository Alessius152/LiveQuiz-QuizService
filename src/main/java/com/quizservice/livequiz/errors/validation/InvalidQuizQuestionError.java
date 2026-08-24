package com.quizservice.livequiz.errors.validation;

import java.util.ArrayList;

public class InvalidQuizQuestionError {
	
	private short qIndex;
	private ArrayList<InvalidQuizQuestionReason> reasons;
	
	public InvalidQuizQuestionError(short qIndex, ArrayList<InvalidQuizQuestionReason> reasons) {
		this.qIndex = qIndex;
		this.reasons = reasons;
	}

	public ArrayList<InvalidQuizQuestionReason> getReasons() {
		return reasons;
	}

	public void setReasons(ArrayList<InvalidQuizQuestionReason> reasons) {
		this.reasons = reasons;
	}

	public short getqIndex() {
		return qIndex;
	}

	public void setqIndex(short qIndex) {
		this.qIndex = qIndex;
	}

	
}
