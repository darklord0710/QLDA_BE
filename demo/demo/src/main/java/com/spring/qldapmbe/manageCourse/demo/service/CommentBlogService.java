package com.spring.qldapmbe.manageCourse.demo.service;

import java.util.List;

import com.spring.qldapmbe.manageCourse.demo.entity.Blog;
import com.spring.qldapmbe.manageCourse.demo.entity.CommentBlog;



public interface CommentBlogService {

	void saveCommentBlog(CommentBlog cb);

	List<CommentBlog> findAllParentCommentBlog(Blog blog);

	Integer countCommentBlogByBlog(Blog blog);

	List<CommentBlog> findAllChildCommentBlogByParentId(Blog blog, Integer parentId);

	Integer countChildCommentBlogByParentId(Blog blog, Integer parentId);
}
