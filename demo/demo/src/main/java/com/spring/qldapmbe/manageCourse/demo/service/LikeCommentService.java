package com.spring.qldapmbe.manageCourse.demo.service;

import com.spring.qldapmbe.manageCourse.demo.entity.Comment;
import com.spring.qldapmbe.manageCourse.demo.entity.LikeComment;
import com.spring.qldapmbe.manageCourse.demo.entity.User;

public interface LikeCommentService {

	void saveLikeComment(LikeComment likeComment);

	LikeComment findLikeCommentByUserAndComment(User user, Comment comment);

	Integer countLikeCommentByComment(Comment comment);

}
