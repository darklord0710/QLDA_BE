package com.spring.qldapmbe.manageCourse.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.qldapmbe.manageCourse.demo.entity.Blog;
import com.spring.qldapmbe.manageCourse.demo.entity.LikeBlog;
import com.spring.qldapmbe.manageCourse.demo.entity.User;
import com.spring.qldapmbe.manageCourse.demo.repository.LikeBlogRepository;
import com.spring.qldapmbe.manageCourse.demo.service.LikeBlogService;

import jakarta.transaction.Transactional;

@Service
public class LikeBlogServiceImpl implements LikeBlogService {

	@Autowired
	private LikeBlogRepository likeBlogRepository;

	@Override
	@Transactional
	public void saveLikeBlog(LikeBlog likeBlog) {
		likeBlogRepository.save(likeBlog);
	}

	@Override
	public LikeBlog findLikeBlogByUserAndBlog(User user, Blog blog) {
		return likeBlogRepository.findLikeBlogByUserAndBlog(user, blog);
	}

	@Override
	@Transactional
	public void deleteLikeBlog(LikeBlog likeBlog) {
		likeBlogRepository.delete(likeBlog);
	}

	@Override
	public Integer countLikeBlogByBlog(Blog blog) {
		return likeBlogRepository.countLikeBlogByBlog(blog);
	}


}
