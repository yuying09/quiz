package com.example.quiz.constants;

public enum ResMessage {
	//常數全大寫，不同單字用底線連接
	SUCCESS(200,"Success!"),//
	DATE_FORMAT_ERROR (400,"Date Format Error!"),//
	OPTIONS_INSUFFICIENT(400,"Options Insufficient!"),//
	TEXT_HAS_OPTIONS(400,"Text has options!"),//
	QUIZ_ID_MISMATCH(400,"Quiz id Mismatch!"),//
	QUIZ_ID_ERROR(400,"Quiz id Error!"),
	QUIZ_NOT_FOUND(404,"Quiz Not Found!"),//
	QUIZ_CANNOT_UPDATE(400,"Quiz Can not update!"),//
	QUIZ_CANNOT_DELETE(400,"Quiz Can not delete!"),//
	QUIZ_UPDATE_FAILED(400,"Quiz Update failed!"),//
	QUESTION_TYPE_ERROR(400,"Question type error!"),//
	ANSWER_IS_REQUIRED(400,"Answer Is Required"),//
	OPTION_ANSWER_MISMATCH(400,"Option Answer Mismatch"),//
	EMAIL_DUPLICATED(400,"Email Duplicated")
	;
	//parameter參數
	private int code;

	private String message;

	// 新增建構方法 只限此enum使用 ，enum無法new
	private ResMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	
}
