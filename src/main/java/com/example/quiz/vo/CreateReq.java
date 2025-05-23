package com.example.quiz.vo;

import java.time.LocalDate;
import java.util.List;

import com.example.quiz.constants.ConstantsMessage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateReq {
	
	@NotBlank(message=ConstantsMessage.PARAM_TITLE_ERROR)
	private String title;

	@NotBlank(message=ConstantsMessage.PARAM_DESCRIPTION_ERROR)
	private String description;

	@FutureOrPresent(message=ConstantsMessage.PARAM_START_DATE_ERROR)//未來或當前時間
	@NotNull(message=ConstantsMessage.PARAM_START_DATE_ERROR)
	private LocalDate startDate;

	@NotNull(message=ConstantsMessage.PARAM_END_DATE_ERROR)
	private LocalDate endDate;

	private boolean published;
	
	//當屬性裡有驗證時要加上@valid 屬性裡面的驗證才會生效
	@Valid
	private List<QuestionVo> questionVos;
	
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
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public boolean isPublished() {
		return published;
	}
	public void setPublished(boolean published) {
		this.published = published;
	}
	public List<QuestionVo> getQuestionVos() {
		return questionVos;
	}
	public void setQuestionVos(List<QuestionVo> questionVos) {
		this.questionVos = questionVos;
	}

	

	
}
