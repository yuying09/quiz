package com.example.quiz.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.quiz.entity.Question;
import com.example.quiz.entity.QuestionId;

import jakarta.transaction.Transactional;

public interface QuestionDao extends JpaRepository<Question, QuestionId> {

	@Transactional
	@Modifying
	@Query(value="insert into question (quiz_id,quest_id,question,type,required,options) "//
			+ " values(:quiz_id,:quest_id,:question,:type,:required,:options ) ",nativeQuery = true)
	public void insert (//
			@Param("quiz_id") int quizId,//
			@Param("quest_id") int quest_id,//
			@Param("question") String question,//
			@Param("type") String type,//
			@Param("required") boolean required,//
			@Param("options") String options);		
	
	@Query(value=" select * from question where quiz_id = ?1",nativeQuery = true)
	public List<Question> getByQuizId(int quizId);
	
	
	@Transactional
	@Modifying
	@Query(value="delete from question where quiz_id = ?1",nativeQuery = true)
	public void deleteByQuizId(int quizId);
	
	@Transactional
	@Modifying
	@Query(value=" delete  from question where quiz_id in (?1) ",nativeQuery = true)
	public void deleteByQuizIdIn(List<Integer> QuizIdList);
}
