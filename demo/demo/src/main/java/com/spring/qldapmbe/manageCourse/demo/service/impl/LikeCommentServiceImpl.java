package com.spring.qldapmbe.manageCourse.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.qldapmbe.manageCourse.demo.entity.Comment;
import com.spring.qldapmbe.manageCourse.demo.entity.LikeComment;
import com.spring.qldapmbe.manageCourse.demo.entity.User;
import com.spring.qldapmbe.manageCourse.demo.repository.LikeCommentRepository;
import com.spring.qldapmbe.manageCourse.demo.service.LikeCommentService;

import jakarta.transaction.Transactional;

@Service
public class LikeCommentServiceImpl implements LikeCommentService {

	@Autowired
	private LikeCommentRepository likeCommentRepository;

	@Override
	@Transactional
	public void saveLikeComment(LikeComment likeComment) {
		likeCommentRepository.save(likeComment);
	}


	@Override
	public Integer countLikeCommentByComment(Comment comment) {
		return likeCommentRepository.countLikeCommentByComment(comment);
	}


	@Override
	public LikeComment findLikeCommentByUserAndComment(User user, Comment comment) {
		return likeCommentRepository.findLikeCommentByUserAndComment(user, comment);
	}


}
