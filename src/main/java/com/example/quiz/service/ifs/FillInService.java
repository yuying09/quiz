package com.example.quiz.service.ifs;

import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.FeedBackRes;
import com.example.quiz.vo.FillInReq;
import com.example.quiz.vo.StatisticsRes;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface FillInService {

	public BasicRes fillin(FillInReq req) throws Exception;
	
	public FeedBackRes feedBack(int quizId) throws JsonProcessingException;
	
	public StatisticsRes statistics(int quizId) throws JsonProcessingException;
	
}
