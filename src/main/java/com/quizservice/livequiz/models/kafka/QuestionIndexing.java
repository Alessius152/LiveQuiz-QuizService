package com.quizservice.livequiz.models.kafka;

import java.util.ArrayList;
import java.util.List;

import com.quizservice.livequiz.models.httpRequests.QuestionType;

public class QuestionIndexing {
	
	private short qI;
	private QuestionType t;
	private ArrayList<Short> a;
	private List<Short> o;

	public QuestionIndexing(short questionIndex, QuestionType type, List<Short> options, ArrayList<Short> answers) {
		this.qI = questionIndex;
		this.t = type;
		this.o = options;
		this.a = answers;
	}

	public short getQI() {
		return qI;
	}

	public void setQI(short qI) {
		this.qI = qI;
	}

	public QuestionType getT() {
		return t;
	}

	public void setT(QuestionType t) {
		this.t = t;
	}

	public ArrayList<Short> getA() {
		return a;
	}

	public void setA(ArrayList<Short> a) {
		this.a = a;
	}

	public List<Short> getO() {
		return o;
	}

	public void setO(List<Short> o) {
		this.o = o;
	}

	
	
}
