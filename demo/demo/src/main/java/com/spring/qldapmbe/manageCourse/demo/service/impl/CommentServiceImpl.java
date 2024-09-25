package com.spring.qldapmbe.manageCourse.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.qldapmbe.manageCourse.demo.entity.Comment;
import com.spring.qldapmbe.manageCourse.demo.repository.CommentRepository;
import com.spring.qldapmbe.manageCourse.demo.service.CommentService;

import jakarta.transaction.Transactional;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepository;

	@Override
	@Transactional
	public void saveComment(Comment commment) {
		commentRepository.save(commment);
	}

	@Override
	public Comment findCommentById(Integer id) {
		Optional<Comment> comment = commentRepository.findById(id);

		if (!comment.isPresent())
			return null;

		Comment c = comment.get();

		return c;
	}

	@Override
	public List<Comment> findByComment(Comment comment) {
		return commentRepository.findByComment(comment);
	}

	@Override
	public void deleteComment(Comment comment) {
		commentRepository.delete(comment);
	}

}
