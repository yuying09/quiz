package com.example.quiz.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.quiz.Dao.QuestionDao;
import com.example.quiz.Dao.QuizDao;
import com.example.quiz.constants.QuestType;
import com.example.quiz.constants.ResMessage;
import com.example.quiz.entity.Question;
import com.example.quiz.entity.Quiz;
import com.example.quiz.service.ifs.QuizService;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.CreateReq;
import com.example.quiz.vo.DeleteReq;
import com.example.quiz.vo.GetQuestionRes;
import com.example.quiz.vo.QuestionVo;
import com.example.quiz.vo.SearchReq;
import com.example.quiz.vo.SearchRes;
import com.example.quiz.vo.UpdateReq;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public  class QuizServiceImpl implements QuizService {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuestionDao questionDao;

	@Transactional(rollbackOn = Exception.class) // 同時操作兩個Dao的新增
	@Override
	public BasicRes create(CreateReq req) throws Exception {

		// 檢查日期 1.開始日期不能比當天早 2.開始日期不能比結束日期晚
		// 區當天日期的方式LocalDate today = LocalDate.now();
		// 排除法 排除1.開始日期早於當天(已於req中驗證) 排除2.開始日期晚於結束日期
		if (req.getStartDate().isAfter(req.getEndDate())) {
			return new BasicRes(ResMessage.DATE_FORMAT_ERROR.getCode(), //
					ResMessage.DATE_FORMAT_ERROR.getMessage());
		}

		// 檢查問題
		List<QuestionVo> questionVos = req.getQuestionVos();
		BasicRes checkRes = checkQuestions(questionVos);
		if (checkRes != null) {
			return checkRes;
		}

		try {
			// 新增問卷
			quizDao.create(//
					req.getTitle(), //
					req.getDescription(), //
					req.getStartDate(), //
					req.getEndDate(), //
					req.isPublished());

			// 取得本次新增的quiz_id
			// 因為@Transaction尚未將資料提交進資料庫，但實際上SQL語法已經執行
			// 依然可以取到對應的值
			int quizId = quizDao.selectMaxId();
			// 新增問題(多個)
			for (QuestionVo vo : questionVos) {
				// 將vo中的options的資料型態由List<String>轉為String
				String optionsStr = mapper.writeValueAsString(vo.getOptions());
				questionDao.insert(quizId, //
						vo.getQuestId(), //
						vo.getQuestion(), //
						vo.getType(), //
						vo.isRequired(), //
						optionsStr);
			}
		} catch (Exception e) {
			// 將exception拋出
			throw e;
		}

		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

	private BasicRes checkQuestions(List<QuestionVo> questionVos) {
		for (QuestionVo item : questionVos) {
			// 檢查1.選擇題至少有兩個選項
			String type = item.getType();

			if(!QuestType.checkAllType(type)) {
				return new BasicRes(ResMessage.QUESTION_TYPE_ERROR.getCode(), //
						ResMessage.QUESTION_TYPE_ERROR.getMessage());
			}
			
			
			if (QuestType.checkChoiceType(type)) {// 當判斷為true表示為選擇題(單選或多選)
				// 選擇題的選項不得為null或小於兩個
				if (item.getOptions() == null || item.getOptions().size() < 2) {
					return new BasicRes(ResMessage.OPTIONS_INSUFFICIENT.getCode(), //
							ResMessage.OPTIONS_INSUFFICIENT.getMessage());
				}
			}

			// 2.簡答題選項不得有值，選項可以是null或是沒有內容
			if (type.equalsIgnoreCase(QuestType.TEXT.getType())) {
				// 排除法:排除非null 且內容size>0
				if (item.getOptions() != null && item.getOptions().size() > 0) {
					return new BasicRes(ResMessage.TEXT_HAS_OPTIONS.getCode(), //
							ResMessage.TEXT_HAS_OPTIONS.getMessage());
				}
			}
		}
		return null;
	}

	@Override
	public GetQuestionRes getQuestionByQuizId(int quizId) throws Exception {
		List<Question> questionsList = questionDao.getByQuizId(quizId);
		List<QuestionVo> voList = new ArrayList<>();
		// 把每個Question中的值設定到QuestionVo裡
		for (Question item : questionsList) {
			QuestionVo vo = new QuestionVo();
			vo.setQuizId(item.getQuizId());
			vo.setQuestId(item.getQuestId());
			vo.setQuestion(item.getQuestion());
			vo.setType(item.getType());
			vo.setRequired(item.isRequired());

			// 原本資料型態是List<String>的字串， 轉回原本的格式List<String>
			try {
				List<String> options = mapper.readValue(item.getOptions(), new TypeReference<>() {
				});
				vo.setOptions(options);
			} catch (Exception e) {
				throw e;
			}
			voList.add(vo);
		}
		return new GetQuestionRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(), voList);
	}
	@Transactional(rollbackOn = Exception.class)
	@Override
	public BasicRes update(UpdateReq req) throws Exception {
		// 檢查表quiz的Id和表question 的quiz_id是否一致
		List<QuestionVo> questionVos = req.getQuestionVos();
		for (QuestionVo vo : questionVos) {
			if (vo.getQuizId() != req.getQuizId()) {
				return new BasicRes(ResMessage.QUIZ_ID_MISMATCH.getCode(), //
						ResMessage.QUIZ_ID_MISMATCH.getMessage());
			}
		}

		if (req.getStartDate().isAfter(req.getEndDate())) {
			return new BasicRes(ResMessage.DATE_FORMAT_ERROR.getCode(), //
					ResMessage.DATE_FORMAT_ERROR.getMessage());
		}

		BasicRes checkRes = checkQuestions(questionVos);
		if (checkRes != null) {
			return checkRes;
		}

		//檢查問卷是否能更新 問卷狀態為進行中或已結束時不可更新
		Quiz quiz = quizDao.selectById(req.getQuizId());
		if(quiz==null) {
			return new BasicRes(ResMessage.QUIZ_NOT_FOUND.getCode(), //
					ResMessage.QUIZ_NOT_FOUND.getMessage());
		}
		if(quiz.isPublished()) {
			LocalDate nowDate = LocalDate.now();
			LocalDate startDate = quiz.getStartDate();
			
			//只要判斷當前日期不在開始日期前，就是進行中or已結束		
			if(!nowDate.isBefore(startDate)) {
				return new BasicRes(ResMessage.QUIZ_CANNOT_UPDATE.getCode(), //
						ResMessage.QUIZ_CANNOT_UPDATE.getMessage());
			}
		}
		
		try {
			// 更新問卷
			int res = quizDao.update(req.getQuizId(), req.getTitle(), req.getDescription(), //
					req.getStartDate(), req.getEndDate(), req.isPublished());

			// 因為問卷的id 是pk值，所以更新成功回傳1，沒有資料更新成功回傳0
			if (res != 1) {
				return new BasicRes(ResMessage.QUIZ_UPDATE_FAILED.getCode(), //
						ResMessage.QUIZ_UPDATE_FAILED.getMessage());
			}

			// 更新問題 步驟1.刪除相同quiz_id的問題 步驟2.新增問題
			questionDao.deleteByQuizId(req.getQuizId());
			// 新增問題(多個)
			for (QuestionVo vo : questionVos) {
				// 將vo中的options的資料型態由List<String>轉為String
				String optionsStr = mapper.writeValueAsString(vo.getOptions());
				questionDao.insert(req.getQuizId(), //
						vo.getQuestId(), //
						vo.getQuestion(), //
						vo.getType(), //
						vo.isRequired(), //
						optionsStr);
			}
		} catch (Exception e) {
			throw e;
		}

		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}
	
	@Transactional(rollbackOn = Exception.class)
	@Override
	public BasicRes delete(DeleteReq req) {
		//檢查問卷是否能刪除 問卷狀態為進行中或已結束時不可刪除
		List<Quiz> quizList = quizDao.selectByIdIn(req.getIdList());
		
		for(Quiz item : quizList) {
			LocalDate nowDate = LocalDate.now();
			LocalDate startDate = item.getStartDate();
			
			//只要判斷當前日期不在開始日期前，就是進行中or已結束		
			if(!nowDate.isBefore(startDate)) {
				return new BasicRes(ResMessage.QUIZ_CANNOT_DELETE.getCode(), //
						ResMessage.QUIZ_CANNOT_DELETE.getMessage());
			}
		}
		
		//刪除資料
		quizDao.DeleteByIdIn(req.getIdList());
		questionDao.deleteByQuizIdIn(req.getIdList());
		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

	
	@Override
	public SearchRes getAllBySearch(SearchReq req) {
		//轉換參數值 
		
		//將null、空字串、全空白字串都轉為空字串
		String quizName = req.getQuizName();
		if(!StringUtils.hasText(quizName)) {
			quizName="";
		}
		
		//處理開始、結束日期為null的情況
		LocalDate startDate = req.getStartDate();
		LocalDate endDate = req.getEndDate();
		
		if(startDate == null) {
			//電腦可以設定的最早年份是1970
			startDate =LocalDate.of(1970, 1, 1);			
		}
		
		if(endDate==null) {
			endDate = LocalDate.of(2999, 12, 31);
		}
		List<Quiz> resList =new ArrayList<>();
		//決定跑前台或後台
		if(req.isForFront()) {
			resList =quizDao.selectAllForFrount(quizName, startDate, endDate);
		}else {
			resList =quizDao.selectAllForBack(quizName, startDate, endDate);
		}
		
		return new SearchRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(),resList);
	}

	@Override
	public SearchRes getAll() {
		return new SearchRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(),quizDao.selectAll());
	}

	
	
	
}
