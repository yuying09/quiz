package com.example.quiz.vo;

import java.util.List;

import com.example.quiz.constants.ConstantsMessage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class FillInReq {

	@NotBlank(message=ConstantsMessage.USER_NAME_IS_NECESSARY)
	private String userName;

	@NotBlank(message=ConstantsMessage.PHONE_IS_NECESSARY)
	private String phone;

	@NotBlank(message=ConstantsMessage.EMAIL_IS_NECESSARY)
	private String email;

	@Min(value=10,message=ConstantsMessage.USER_AGE_ERROR)
	private int age;

	@Min(value=1,message=ConstantsMessage.PARAM_QUIZ_ID_ERROR)
	private int quizId;
	

	
	@Valid
	@NotEmpty(message=ConstantsMessage.ANSWER_IS_NECESSARY)
	private List<QuestIdAnswerVo> answerVoList;


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

	public List<QuestIdAnswerVo> getAnswerVoList() {
		return answerVoList;
	}

	public void setAnswerVoList(List<QuestIdAnswerVo> answerVoList) {
		this.answerVoList = answerVoList;
	}

}
