package com.spring.qldapmbe.manageCourse.demo.service;

import com.spring.qldapmbe.manageCourse.demo.entity.Blog;
import com.spring.qldapmbe.manageCourse.demo.entity.LikeBlog;
import com.spring.qldapmbe.manageCourse.demo.entity.User;

public interface LikeBlogService {

	void saveLikeBlog(LikeBlog likeBlog);

	LikeBlog findLikeBlogByUserAndBlog(User user, Blog blog);

	void deleteLikeBlog(LikeBlog likeBlog);

	Integer countLikeBlogByBlog(Blog blog);

}
