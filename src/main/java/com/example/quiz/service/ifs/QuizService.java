package com.example.quiz.service.ifs;

import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.CreateReq;
import com.example.quiz.vo.DeleteReq;
import com.example.quiz.vo.GetQuestionRes;
import com.example.quiz.vo.SearchReq;
import com.example.quiz.vo.SearchRes;
import com.example.quiz.vo.UpdateReq;

public interface QuizService {

	public BasicRes create(CreateReq req) throws Exception;
	
	public SearchRes getAll();
	
	public SearchRes getAllBySearch(SearchReq req);
	
	public GetQuestionRes getQuestionByQuizId(int quizId) throws Exception;
	
	public BasicRes update(UpdateReq req) throws Exception;
	
	public BasicRes delete (DeleteReq req);
}
