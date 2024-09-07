package com.spring.qldapmbe.manageCourse.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.qldapmbe.manageCourse.demo.entity.Comment;
import com.spring.qldapmbe.manageCourse.demo.entity.LikeComment;
import com.spring.qldapmbe.manageCourse.demo.entity.User;



@Repository
public interface LikeCommentRepository extends JpaRepository<LikeComment, Integer> {

	@Query("SELECT lc FROM LikeComment lc WHERE lc.user = :user and lc.comment = :comment")
	LikeComment findLikeCommentByUserAndComment(@Param("user") User user,
			@Param("comment") Comment comment);

	@Query("SELECT COUNT(lc) FROM LikeComment lc WHERE lc.comment = :comment and isActived = true")
	Integer countLikeCommentByComment(@Param("comment") Comment comment);
}
