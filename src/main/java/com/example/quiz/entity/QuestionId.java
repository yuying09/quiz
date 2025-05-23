package com.example.quiz.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class QuestionId implements Serializable{

	private int quizId;

	
	private int questId;


	public int getQuizId() {
		return quizId;
	}


	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}


	public int getQuestId() {
		return questId;
	}


	public void setQuestId(int questId) {
		this.questId = questId;
	}
	
	
}
