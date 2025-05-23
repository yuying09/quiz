package com.example.quiz.vo;

import java.util.List;

public class FeedBackRes extends BasicRes {

	private String title;

	private String description;

	private List<FeedBackVo> feedBackVoList;

	public FeedBackRes() {
		super();
	}

	public FeedBackRes(int code, String message) {
		super(code, message);		
	}

	public FeedBackRes(int code, String message,String title,//
			String description, List<FeedBackVo> feedBackVoList) {
		super(code, message);
		this.title = title;
		this.description = description;
		this.feedBackVoList = feedBackVoList;
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

	public List<FeedBackVo> getFeedBackVoList() {
		return feedBackVoList;
	}

	public void setFeedBackVoList(List<FeedBackVo> feedBackVoList) {
		this.feedBackVoList = feedBackVoList;
	}

}
