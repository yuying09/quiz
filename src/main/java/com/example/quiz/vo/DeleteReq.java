package com.example.quiz.vo;

import java.util.List;

import com.example.quiz.constants.ConstantsMessage;

import jakarta.validation.constraints.NotEmpty;

public class DeleteReq {

	@NotEmpty(message = ConstantsMessage.PARAM_QUEST_ID_ERROR)
	private List<Integer> idList;

	public List<Integer> getIdList() {
		return idList;
	}

	public void setIdList(List<Integer> idList) {
		this.idList = idList;
	}
	
	
	
}
