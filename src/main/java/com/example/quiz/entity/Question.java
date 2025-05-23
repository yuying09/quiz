package com.example.quiz.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "question")
@IdClass(value= QuestionId.class) 
public class Question {

	@Id
	@Column(name = "quiz_id")
	private int quizId;

	@Id
	@Column(name = "quest_id")
	private int questId;

	@Column(name = "question")
	private String question;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "required")
	private boolean required;
	
	@Column(name = "options")
	private String options;

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

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}
	
	
}
