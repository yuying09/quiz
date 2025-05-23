package com.example.quiz.Dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.quiz.entity.FillIn;
import com.example.quiz.entity.FillInId;
import com.example.quiz.vo.FeedBackDto;

import jakarta.transaction.Transactional;

@Repository
public interface FillInDao extends JpaRepository<FillIn, FillInId> {

	@Query(value = "select count(email) from feedBack where email = ?1 and quiz_id =?2", //
			nativeQuery = true)
	public int selectCountByEmailAndQuizId(String email, int quizId);

	@Transactional
	@Modifying
	@Query(value = "insert into feedBack "//
			+ " (user_name,phone,email,age,quiz_id,quest_id,answer,fillin_date)"//
			+ " values"//
			+ " (:user_name,:phone,:email,:age,:quiz_id,:quest_id,:answer,:fillin_date) "//
			, nativeQuery = true)
	public void insert(//
			@Param("user_name") String user_name, //
			@Param("phone") String phone, //
			@Param("email") String email, //
			@Param("age") int age, //
			@Param("quiz_id") int quizId, //
			@Param("quest_id") int questId, //
			@Param("answer") String answer, //
			@Param("fillin_date") LocalDate fillinDate);

    /**
     * nativeQuery = false時 SQL語法中<br>
     * 1.select的欄位名稱會變成 FeedBackDto (要存進的檔案裡)的屬性變數名稱<br>
     * 2.on後面的欄位名稱會是Entity class中的屬性變數名稱<br>
     * 3.表的名稱會變成 Entity class名稱<br>
     * 4.select的欄位透過new建構方法的方式塞值，FeedbackDto中也要有對應的建構方法<br>
     * 5.FeedBackDto要給定完整路徑
     * @param quizId
     * @return
     */
	// 
	@Query(value = " select new com.example.quiz.vo.FeedBackDto(Qz.title , Qz.description ,"//
			+ " Qu.questId , Qu.question , "//
			+ " F.userName, F.phone, F.email,F.age,F.answer,F.fillInDate)"//
			+ " from Quiz as Qz "//
			+ " join Question as Qu on Qz.id = Qu.quizId"//
			+ " join FillIn as F on Qu.quizId = F.quizId where Qz.id =?1"
			, nativeQuery = false)
	public List<FeedBackDto> selectJoinByQuizId(int quizId);
	
	@Query(value = "select * from feedBack where quiz_id =?1", //
			nativeQuery = true)
	public List<FillIn> selectByQuizId(int quizId);
}
