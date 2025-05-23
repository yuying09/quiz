package com.example.quiz.vo;

import java.util.List;

import com.example.quiz.constants.ConstantsMessage;

import jakarta.validation.constraints.Min;

public class QuestIdAnswerVo {

	@Min(value=1,message=ConstantsMessage.PARAM_QUEST_ID_ERROR)
	private int questId;
	
	//考慮到簡答題與非必填的可能性，答案不檢查
	private List<String> answerList;

	
	public int getQuestId() {
		return questId;
	}

	public void setQuestId(int questId) {
		this.questId = questId;
	}


	public List<String> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<String> answerList) {
		this.answerList = answerList;
	}
	
	

	
}
