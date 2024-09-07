package com.spring.qldapmbe.manageCourse.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.spring.qldapmbe.manageCourse.demo.Dto.BlogDtoOutputById;
import com.spring.qldapmbe.manageCourse.demo.entity.Blog;
import com.spring.qldapmbe.manageCourse.demo.entity.User;



public interface BlogService {

	void saveBlog(Blog blog);

	List<Blog> findAllBlogs();

	Blog findById(Integer blogId);

	List<Blog> findAllBlogsByUser(User user);

	Integer countBlogByCurrentUser(User user);

	Page<BlogDtoOutputById> allBlogsPaginated(Integer page, Integer size,
			List<BlogDtoOutputById> data);

}
