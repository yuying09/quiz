package com.example.quiz.vo;

import java.util.List;

public class GetQuestionRes extends BasicRes {

	private List<QuestionVo> questionList;

	public GetQuestionRes() {
		super();

	}

	public GetQuestionRes(int code, String message) {
		super(code, message);

	}

	public GetQuestionRes(int code, String message,List<QuestionVo> questionList) {
		super(code, message);
		this.questionList = questionList;
	}

	public List<QuestionVo> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<QuestionVo> questionList) {
		this.questionList = questionList;
	}
	
	
	
}
