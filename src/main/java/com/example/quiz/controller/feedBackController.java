package com.example.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz.service.ifs.FillInService;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.FeedBackRes;
import com.example.quiz.vo.FillInReq;
import com.example.quiz.vo.StatisticsRes;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.validation.Valid;
@RequestMapping(value = "quiz/")
@CrossOrigin //解決跨網域請求錯誤
@RestController
public class feedBackController {
	
	@Autowired
	private FillInService fillInService;
	
	@PostMapping(value="fillIn")
	public BasicRes getAll(@Valid @RequestBody FillInReq req) throws Exception {
		return fillInService.fillin(req);
	}
	
	@PostMapping(value="feedBack")
	public FeedBackRes getAll(@RequestParam("quizId")int quizId) throws Exception {
		return fillInService.feedBack(quizId);
	}
	
	@PostMapping(value="statistics")
	public StatisticsRes statistics(@RequestParam("quizId")int quizId) throws JsonProcessingException{
		return fillInService.statistics(quizId);
	}
}
