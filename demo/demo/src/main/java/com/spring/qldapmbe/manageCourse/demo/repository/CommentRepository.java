package com.spring.qldapmbe.manageCourse.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.qldapmbe.manageCourse.demo.entity.Comment;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

	List<Comment> findByComment(Comment comment);
}
