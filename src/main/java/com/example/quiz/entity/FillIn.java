package com.example.quiz.entity;

import java.time.LocalDate;

import jakarta.persistence.*;


@Entity
@Table(name="feedback")
@IdClass(value= FillInId.class)
public class FillIn {

	@Column(name="user_name")
	private String userName;
	
	@Column(name="phone")
	private String phone;

	@Id
	@Column(name="email")
	private String email;
	
	@Column(name="age")
	private int age;
	
	@Id
	@Column(name="quiz_id")
	private int quizId;
	
	@Id
	@Column(name="quest_id")
	private int questId;
	
	@Column(name="answer")
	private String answer;
	
	@Column(name="fillin_date")
	private LocalDate fillInDate;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

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

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public LocalDate getFillInDate() {
		return fillInDate;
	}

	public void setFillInDate(LocalDate fillInDate) {
		this.fillInDate = fillInDate;
	}
	
	
	
	
}
