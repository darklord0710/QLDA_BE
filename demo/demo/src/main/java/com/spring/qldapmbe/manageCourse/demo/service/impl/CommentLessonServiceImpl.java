package com.spring.qldapmbe.manageCourse.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.qldapmbe.manageCourse.demo.entity.CommentLesson;
import com.spring.qldapmbe.manageCourse.demo.repository.CommentLessonRepository;
import com.spring.qldapmbe.manageCourse.demo.service.CommentLessonService;

import jakarta.transaction.Transactional;

@Service
public class CommentLessonServiceImpl implements CommentLessonService {

	@Autowired
	private CommentLessonRepository commentLessonRepository;

	@Override
	@Transactional
	public void saveCommentLesson(CommentLesson commentLesson) {
		commentLessonRepository.save(commentLesson);
	}

	@Override
	public CommentLesson findCommentLessonByid(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}


}
