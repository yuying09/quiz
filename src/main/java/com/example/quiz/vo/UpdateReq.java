package com.example.quiz.vo;

import com.example.quiz.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;

public class UpdateReq extends CreateReq{

	@Min(value = 1,message = ConstantsMessage.PARAM_QUIZ_ID_ERROR)
	private int quizId;

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}
	
	
	
}
