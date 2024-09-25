package com.spring.qldapmbe.manageCourse.demo.service;

import java.util.List;

import com.spring.qldapmbe.manageCourse.demo.entity.Comment;

public interface CommentService {

	void saveComment(Comment commment);

	Comment findCommentById(Integer id);

	List<Comment> findByComment(Comment comment);

	void deleteComment(Comment comment);

}
