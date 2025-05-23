package com.example.quiz.Dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.quiz.entity.Quiz;

import jakarta.transaction.Transactional;


@Repository
public interface QuizDao extends JpaRepository<Quiz, Integer>{
	@Transactional
	@Modifying
	@Query(value="insert into quiz (title,description,start_date,end_date,published) "//
			+ " values(:title,:description,:start_date,:end_date,:published ) ",nativeQuery = true)
	public void create (//
			@Param("title") String title,//
			@Param("description")String description ,//
			@Param("start_date") LocalDate startDate,//
			@Param("end_date")LocalDate endDate,//
			@Param("published")boolean published);
	

	//因為表quiz的id為流水號，最後新增的一筆id為最大值
	@Query(value=" select max(id) from quiz ",nativeQuery = true)
	public int selectMaxId() ;
	
	@Query(value=" select * from quiz ",nativeQuery = true)
	public List<Quiz> selectAll() ;
	
	@Query(value=" select * from quiz ",nativeQuery = true)
	public List<Quiz> selectAllForBack(String quizName,LocalDate startDate , LocalDate endDate) ;
	
	@Query(value=" select * from quiz where title like %?1% and start_date >=?2 "//
			+ " and end_date<= ?3 and published is true",nativeQuery = true)
	public List<Quiz> selectAllForFrount(String quizName,LocalDate startDate , LocalDate endDate ) ;
	
	@Transactional
	@Modifying
	@Query(value="update quiz set title = :title,"//
			+ " description= :description,"//
			+ " start_date = :start_date,"//
			+ " end_date = :end_date,"//
			+ " published = :published"//
			+ " where id = :id",nativeQuery = true)
	public int update (//
			@Param("id") int id,//
			@Param("title") String title,//
			@Param("description")String description ,//
			@Param("start_date") LocalDate startDate,//
			@Param("end_date")LocalDate endDate,//
			@Param("published")boolean published);
	
	@Query(value=" select * from quiz where id = ?1 ",nativeQuery = true)
	public Quiz selectById(int id);
	
	@Query(value=" select count(id) from quiz where id = ?1 and ?2 >=start_date"//
			+ " and ?2<= end_date and published is true ",nativeQuery = true)
	public int selectCountById(int id, LocalDate now );
	
	@Query(value=" select * from quiz where id in (?1) ",nativeQuery = true)
	public List<Quiz> selectByIdIn(List<Integer> idList);
	
	@Transactional
	@Modifying
	@Query(value=" delete  from quiz where id in (?1) ",nativeQuery = true)
	public void DeleteByIdIn(List<Integer> idList);
	
	@Query(value=" select count(id) from quiz where id = ?1",nativeQuery = true)
	public int selectCountById(int id);
	
}

