package com.example.quiz.vo;

import java.time.LocalDate;
import java.util.List;

//一個FeedBackVo 代表一個使用者的填答資訊
public class FeedBackVo {

	private String userName;

	private String phone;

	private String email;

	private int age;

	private LocalDate fillInDate;

	private List<QuestAnswerVo> answerVoList;

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

	public List<QuestAnswerVo> getAnswerVoList() {
		return answerVoList;
	}

	public void setAnswerVoList(List<QuestAnswerVo> answerVoList) {
		this.answerVoList = answerVoList;
	}

	public LocalDate getFillInDate() {
		return fillInDate;
	}

	public void setFillInDate(LocalDate fillInDate) {
		this.fillInDate = fillInDate;
	}

}
