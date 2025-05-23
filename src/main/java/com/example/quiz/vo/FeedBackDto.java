package com.example.quiz.vo;

import java.time.LocalDate;

//DTO(Data Transfer Object)
//DTO: 命名為 DTO 的原因是這些資料主要是從資料庫來，且無法只用一個 Entity 裝載，一般是用 join 取多張表的欄位資料
//VO: 也可以命名為 VO(Value Object): 用來裝資料容器的統稱
//Entity: 也是裝資料的容器，但 Entity 可以 mapping 到資料表及其資料欄位

public class FeedBackDto {
	// 一個 FeedBackDto 表示一筆資料，資料來自於3張表不同的欄位

	private String title;

	private String description;

	private int questId;

	private String question;

	private String userName;

	private String phone;

	private String email;

	private int age;

	private String answer;

	private LocalDate fillinDate;

	public FeedBackDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FeedBackDto(String title, String description, int questId, String question, String userName, String phone,
			String email, int age, String answer, LocalDate fillinDate) {
		super();
		this.title = title;
		this.description = description;
		this.questId = questId;
		this.question = question;
		this.userName = userName;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.answer = answer;
		this.fillinDate = fillinDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public LocalDate getFillinDate() {
		return fillinDate;
	}

	public void setFillinDate(LocalDate fillinDate) {
		this.fillinDate = fillinDate;
	}

}
