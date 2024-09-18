package com.spring.qldapmbe.manageCourse.demo.service;

import com.spring.qldapmbe.manageCourse.demo.entity.CommentLesson;

public interface CommentLessonService {

	void saveCommentLesson(CommentLesson commentLesson);

	CommentLesson findCommentLessonByid(Integer id);

}
