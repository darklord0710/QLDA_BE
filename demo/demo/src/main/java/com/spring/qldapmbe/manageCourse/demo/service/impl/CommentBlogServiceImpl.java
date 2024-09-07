package com.spring.qldapmbe.manageCourse.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.qldapmbe.manageCourse.demo.entity.Blog;
import com.spring.qldapmbe.manageCourse.demo.entity.CommentBlog;
import com.spring.qldapmbe.manageCourse.demo.repository.CommentBlogRepository;
import com.spring.qldapmbe.manageCourse.demo.service.CommentBlogService;

import jakarta.transaction.Transactional;

@Service
public class CommentBlogServiceImpl implements CommentBlogService {

	@Autowired
	private CommentBlogRepository commentBlogRepository;

	@Override
	@Transactional
	public void saveCommentBlog(CommentBlog cb) {
		commentBlogRepository.save(cb);
	}

	@Override
	public List<CommentBlog> findAllParentCommentBlog(Blog blog) {
		return commentBlogRepository.findAllParentCommentBlog(blog);
	}

	@Override
	public Integer countCommentBlogByBlog(Blog blog) {
		return commentBlogRepository.countCommentBlogByBlog(blog);
	}

	@Override
	public List<CommentBlog> findAllChildCommentBlogByParentId(Blog blog, Integer parentId) {
		return commentBlogRepository.findAllChildCommentBlogByParentId(blog, parentId);
	}

	@Override
	public Integer countChildCommentBlogByParentId(Blog blog, Integer parentId) {
		return commentBlogRepository.countChildCommentBlogByParentId(blog, parentId);
	}

}
