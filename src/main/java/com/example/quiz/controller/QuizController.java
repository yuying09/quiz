package com.example.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz.service.ifs.QuizService;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.CreateReq;
import com.example.quiz.vo.DeleteReq;
import com.example.quiz.vo.GetQuestionRes;
import com.example.quiz.vo.SearchReq;
import com.example.quiz.vo.SearchRes;
import com.example.quiz.vo.UpdateReq;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
//@RequestMapping(value = "quiz/"): 表示此 controller 底下的所有 API 路徑的前綴會是以 quiz/開頭
//即預設的路徑會是 localhost:8080/quiz/

@RequestMapping(value = "quiz/")
@CrossOrigin //解決跨網域請求錯誤
@RestController
public class QuizController {

	@Autowired
	private QuizService quizService;
	
	@PostMapping(value="create")
	public BasicRes create(@RequestBody @Valid CreateReq req)throws Exception{
		return quizService.create(req);
	}
	
	@GetMapping(value="getAll")
	public SearchRes getAll() {
		return quizService.getAll();
	}
	
	
	@PostMapping(value="getAllBySearch")
	public SearchRes getAllBySearch(@RequestBody SearchReq req) {
		return quizService.getAllBySearch(req);
	}
	
	//API路徑 http://localhost:8080/quiz/getByQuizId?quizId=1
	@GetMapping(value="getByQuizId")
	public GetQuestionRes getQuestionByQuizId(@RequestParam("quizId") int quizId) throws Exception {
		return quizService.getQuestionByQuizId(quizId);
	}
	
	@PostMapping(value="update")
	public BasicRes update(@RequestBody @Valid UpdateReq req) throws Exception{
		return quizService.update(req);
	}
	
	@Hidden
	@PostMapping(value="delete")
	public BasicRes delete (@RequestBody @Valid DeleteReq req) {
		return quizService.delete(req);
	}
	
}
