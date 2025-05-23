package com.example.quiz.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quiz.Dao.FillInDao;
import com.example.quiz.Dao.QuestionDao;
import com.example.quiz.Dao.QuizDao;
import com.example.quiz.constants.QuestType;
import com.example.quiz.constants.ResMessage;
import com.example.quiz.entity.FillIn;
import com.example.quiz.entity.Question;
import com.example.quiz.entity.Quiz;
import com.example.quiz.service.ifs.FillInService;
import com.example.quiz.vo.BasicRes;
import com.example.quiz.vo.FeedBackRes;
import com.example.quiz.vo.FeedBackVo;
import com.example.quiz.vo.FillInReq;
import com.example.quiz.vo.OptionCountVo;
import com.example.quiz.vo.QuestAnswerVo;
import com.example.quiz.vo.QuestIdAnswerVo;
import com.example.quiz.vo.StatisticsRes;
import com.example.quiz.vo.StatisticsVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class FillInServiceImpl implements FillInService {

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private FillInDao fillInDao;

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuestionDao questionDao;

	@Transactional(rollbackOn = Exception.class)
	@Override
	public BasicRes fillin(FillInReq req) throws Exception {
		// 檢查問卷:1.是否存在 2.問卷是否已發布並允許開放填寫
		int quizId = req.getQuizId();

		LocalDate now = LocalDate.now();

		int quizCount = quizDao.selectCountById(quizId, now);

		if (quizCount != 1) {
			return new BasicRes(ResMessage.QUIZ_NOT_FOUND.getCode(), //
					ResMessage.QUIZ_NOT_FOUND.getMessage());
		}

		// 檢查問題 :1.答案是否與選項相符 2.必填題是否有答案 (用questId 撈全部減少與資料庫連結次數)
		// 為了檢查將List<QuestIdAnswerVo>轉為map
		Map<Integer, List<String>> questIdAnswerMap = new HashMap<>();
		for (QuestIdAnswerVo vo : req.getAnswerVoList()) {
			questIdAnswerMap.put(vo.getQuestId(), vo.getAnswerList());
		}

		List<Question> questionList = questionDao.getByQuizId(quizId);

		for (Question quest : questionList) {
			// 排除:沒有作答的必填題
			// !questIdAnswerMap.containsKey(quest.getQuestId()) 表示map中沒有該題號及該題的答案
			if (quest.isRequired() && !questIdAnswerMap.containsKey(quest.getQuestId())) {
				return new BasicRes(ResMessage.ANSWER_IS_REQUIRED.getCode(), //
						ResMessage.ANSWER_IS_REQUIRED.getMessage());
			}

			// 判斷 是否為選擇題，若是選擇題需要轉換型態
			// 跳過簡答題的題型
			if (quest.getType().equalsIgnoreCase(QuestType.TEXT.getType())) {
				continue;
			}
			// 原本資料型態是List<String>的字串， 轉回原本的格式List<String>
			try {
				List<String> options = mapper.readValue(quest.getOptions(), new TypeReference<>() {
				});
				List<String> answerList = questIdAnswerMap.get(quest.getQuestId());

				if (!options.containsAll(answerList)) {
					return new BasicRes(ResMessage.OPTION_ANSWER_MISMATCH.getCode(), //
							ResMessage.OPTION_ANSWER_MISMATCH.getMessage());
				}
			} catch (Exception e) {
				throw e;
			}
		}

		// 3.檢查使用者是否已填寫過此筆問卷
		int emailCount = fillInDao.selectCountByEmailAndQuizId(req.getEmail(), req.getQuizId());

		if (emailCount > 0) {
			return new BasicRes(ResMessage.EMAIL_DUPLICATED.getCode(), //
					ResMessage.EMAIL_DUPLICATED.getMessage());
		}

		// 存資料
		try {
			for (QuestIdAnswerVo vo : req.getAnswerVoList()) {
				String answerStr = mapper.writeValueAsString(vo.getAnswerList());
				fillInDao.insert(req.getUserName(), req.getPhone(), req.getEmail(), //
						req.getAge(), req.getQuizId(), vo.getQuestId(), answerStr, now);
			}
		} catch (Exception e) {
			throw e;
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

	@Override
	public FeedBackRes feedBack(int quizId) throws JsonProcessingException {
		// quiz
		Quiz quiz = quizDao.selectById(quizId);
		if (quiz == null) {
			return new FeedBackRes(ResMessage.QUIZ_NOT_FOUND.getCode(), //
					ResMessage.QUIZ_NOT_FOUND.getMessage());
		}

		// question
		List<Question> questionList = questionDao.getByQuizId(quizId);
		// 問題編號與問題的map
		Map<Integer, String> map = new HashMap<>();
		// 將每個Question 中的問題編號和問題設定到QuestAnswerVo 中
		for (Question item : questionList) {
			map.put(item.getQuestId(), item.getQuestion());
		}

		// 3.feedBack
		List<FillIn> feedBackList = fillInDao.selectByQuizId(quizId);
		// feedBackList 的結果可能有重複地填寫者資訊，要先經過整理
		// email和FeedBackVo的map
		Map<String, FeedBackVo> emailMap = new HashMap<>();
		for (FillIn item : feedBackList) {
			// 判斷email 是否已存在於emailMap中
			if (emailMap.containsKey(item.getEmail())) {
				// 判斷email 已存在於emailMap中，將對應的FeedBackVo取出
				FeedBackVo vo = emailMap.get(item.getEmail());
				// 設定作答
				List<QuestAnswerVo> answerVoList = setQuestAns(item, map);
				// 從vo中取出之前的作答
				List<QuestAnswerVo> otherAnswerVoList = vo.getAnswerVoList();
				// 將兩者合併
				otherAnswerVoList.addAll(answerVoList);
				// 將合併的結果設定回vo
				vo.setAnswerVoList(otherAnswerVoList);
				// 將最終結果放回emailMap
				emailMap.put(item.getEmail(), vo);
			} else {
				FeedBackVo vo = new FeedBackVo();
				// 設定填答者資訊
				vo.setUserName(item.getUserName());
				vo.setPhone(item.getPhone());
				vo.setEmail(item.getEmail());
				vo.setAge(item.getAge());
				vo.setFillInDate(item.getFillInDate());
				// 設定作答
				List<QuestAnswerVo> answerVoList = setQuestAns(item, map);
				vo.setAnswerVoList(answerVoList);
				emailMap.put(item.getEmail(), vo);
			}

		}
		List<FeedBackVo> feedBackVoList = new ArrayList<>();

		for (Entry<String, FeedBackVo> item : emailMap.entrySet()) {
			feedBackVoList.add(item.getValue());
		}

		return new FeedBackRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), //
				quiz.getTitle(), quiz.getDescription(), feedBackVoList);
	}

	private List<QuestAnswerVo> setQuestAns(FillIn item, Map<Integer, String> map) throws JsonProcessingException {
		// 設定作答
		List<QuestAnswerVo> answerVoList = new ArrayList<>();
		QuestAnswerVo questAnsVo = new QuestAnswerVo();
		questAnsVo.setQuestId(item.getQuestId());
		// 使用questId當作key從map中取得對應的value(問題)
		String question = map.get(item.getQuestId());
		questAnsVo.setQuestion(question);

		// 取得答案的字串
		String ansStr = item.getAnswer();
		// 將字串轉成List<String>
		try {
			List<String> answerList = mapper.readValue(ansStr, new TypeReference<>() {
			});
			questAnsVo.setAnswerList(answerList);
		} catch (JsonProcessingException e) {
			throw e;
		}
		answerVoList.add(questAnsVo);
		return answerVoList;
	}

	@Override
	public StatisticsRes statistics(int quizId) throws JsonProcessingException {
		// 檢查quizId
		int count = quizDao.selectCountById(quizId);
		if (count != 1) {
			return new StatisticsRes(ResMessage.QUIZ_NOT_FOUND.getCode(), //
					ResMessage.QUIZ_NOT_FOUND.getMessage());
		}

		// 取得問題的相關資料
		List<Question> questionList = questionDao.getByQuizId(quizId);
		List<StatisticsVo> statisticsVoList = new ArrayList<>();
		// 問題編號、選項與對應次數的map
		Map<Integer, Map<String, Integer>> questIdOptionCountMap = new HashMap<>();

		for (Question item : questionList) {
			// 跳過簡答題 簡答題無選項不做統計
			if (item.getType().equalsIgnoreCase(QuestType.TEXT.getType())) {
				continue;
			}
			StatisticsVo vo = new StatisticsVo();
			vo.setQuestId(item.getQuestId());
			vo.setQuestion(item.getQuestion());
			vo.setType(item.getType());
			vo.setRequired(item.isRequired());
			statisticsVoList.add(vo);
			// 設定選項、次數的map
			setOptionMap(item, questIdOptionCountMap);
		}
		// 統計次數
		statistic(quizId, questIdOptionCountMap);
		// 把統計次數放進對應題號中
		for (StatisticsVo voItem : statisticsVoList) {
			int questId = voItem.getQuestId();
			// optionCountMap是每題選項及對應次數
			Map<String, Integer> optionCountMap = questIdOptionCountMap.get(questId);
			// 將optionCountMap 轉成 List<OptionCountVo>
			List<OptionCountVo> ocVoList = new ArrayList<>();
			for (Entry<String, Integer> ocItem : optionCountMap.entrySet()) {
				// 把每個 ocItem 的 key-value轉成單一個OptionCountVo
				OptionCountVo ocVo = new OptionCountVo();
				ocVo.setOption(ocItem.getKey());
				ocVo.setCount(ocItem.getValue());
				ocVoList.add(ocVo);
			}
			voItem.setOptionCountVoList(ocVoList);
			//====================
			//以下是Lambda表達式，效果同上的foreach
//			optionCountMap.forEach((k,v) -> {
//				OptionCountVo ocVo = new OptionCountVo();
//				ocVo.setOption(k);
//				ocVo.setCount(v);
//				ocVoList.add(ocVo);
//			});
		}
		return new StatisticsRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(),statisticsVoList);
	}

	// 因為參考的記憶體位置相同，會同步改變無須回傳
	private void setOptionMap(Question item, Map<Integer, Map<String, Integer>> optionCountMap) //
			throws JsonProcessingException {
		// 先不設定 List<OptionCountVo> 轉換成 optionCountMap
		String optionsStr = item.getOptions();
		// 將字串轉成List<String>
		List<String> optionList = convertStrtoList(optionsStr);
		Map<String, Integer> map = new HashMap<>();
		for (String str : optionList) {
			map.put(str, 0);
		}
		optionCountMap.put(item.getQuestId(), map);

	}

	private List<String> convertStrtoList(String inputStr) throws JsonProcessingException {
		try {
			return mapper.readValue(inputStr, new TypeReference<>() {
			});
		} catch (JsonProcessingException e) {
			throw e;
		}

	}

	private void statistic(int quizId, Map<Integer, Map<String, Integer>> questIdOptionCountMap) //
			throws JsonProcessingException {
		List<FillIn> feedBackList = fillInDao.selectByQuizId(quizId);
		for (FillIn feedBack : feedBackList) {

			Map<String, Integer> optionCountMap = questIdOptionCountMap.get(feedBack.getQuestId());
			// 因為之前跳過簡答題的選項蒐集 所以該簡答題的optionCountMap會是null
			// 所以當是null(簡答題)就跳過不統計
			if (optionCountMap == null) {
				continue;
			}

			// 將answer的String 轉回原本的List<String> 一題的答案有多個表示該題為多選題
			List<String> answerList = convertStrtoList(feedBack.getAnswer());
			// 透過問題編號取得選項與對應次數的map

			for (String answer : answerList) {
				// 取得選項對應次數將次數+1再將更新後次數放回
				optionCountMap.put(answer, optionCountMap.get(answer) + 1);
			}
		}
	}

}
