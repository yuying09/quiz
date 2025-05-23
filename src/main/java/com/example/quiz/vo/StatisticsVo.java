package com.example.quiz.vo;

import java.util.List;

//一個 StatisticsVo 代表一題的統計
public class StatisticsVo {

	private int questId;

	private String question;

	private String type;

	private boolean required;

	private List<OptionCountVo> optionCountVoList;

	

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

	public List<OptionCountVo> getOptionCountVoList() {
		return optionCountVoList;
	}

	public void setOptionCountVoList(List<OptionCountVo> optionCountVoList) {
		this.optionCountVoList = optionCountVoList;
	}

}
