package com.example.quiz.vo;

import java.time.LocalDate;

public class SearchReq {

	private String quizName;

	private LocalDate StartDate;

	private LocalDate endDate;
	
	private boolean forFront;

	public String getQuizName() {
		return quizName;
	}

	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}

	public LocalDate getStartDate() {
		return StartDate;
	}

	public void setStartDate(LocalDate startDate) {
		StartDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public boolean isForFront() {
		return forFront;
	}

	public void setForFront(boolean forFront) {
		this.forFront = forFront;
	}

}
