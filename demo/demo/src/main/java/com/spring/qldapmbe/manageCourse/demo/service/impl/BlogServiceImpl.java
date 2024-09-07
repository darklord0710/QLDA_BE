package com.spring.qldapmbe.manageCourse.demo.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spring.qldapmbe.manageCourse.demo.Dto.BlogDtoOutputById;
import com.spring.qldapmbe.manageCourse.demo.entity.Blog;
import com.spring.qldapmbe.manageCourse.demo.entity.User;
import com.spring.qldapmbe.manageCourse.demo.repository.BlogRepository;
import com.spring.qldapmbe.manageCourse.demo.service.BlogService;

import jakarta.transaction.Transactional;

@Service
public class BlogServiceImpl implements BlogService {

	@Autowired
	private BlogRepository blogRepository;


	@Override
	@Transactional
	public void saveBlog(Blog blog) {
		blogRepository.save(blog);
	}

	@Override
	public Blog findById(Integer blogId) {
		Optional<Blog> blog = blogRepository.findById(blogId);

		if (!blog.isPresent())
			return null;

		Blog b = blog.get();

		return b;
	}

	@Override
	public List<Blog> findAllBlogsByUser(User user) {
		return blogRepository.findAllBlogsByUser(user);
	}

	@Override
	public Integer countBlogByCurrentUser(User user) {
		return blogRepository.countBlogByCurrentUser(user);
	}

	@Override
	public Page<BlogDtoOutputById> allBlogsPaginated(Integer page, Integer size,
			List<BlogDtoOutputById> data) {

		data.sort(Comparator.comparing(BlogDtoOutputById::getCreatedAt).reversed());
		Pageable pageable = PageRequest.of(page - 1, size);

		int start = (int) pageable.getOffset();
		int end = 0;
		List<BlogDtoOutputById> allBlogsPaginated;

		if (data.size() < start) {
			allBlogsPaginated = Collections.emptyList();
		} else {
			end = Math.min((start + pageable.getPageSize()), data.size());
			allBlogsPaginated = data.subList(start, end);
		}

		return new PageImpl<>(allBlogsPaginated, pageable, data.size());
	}

	@Override
	public List<Blog> findAllBlogs() {
		// TODO Auto-generated method stub
		return null;
	}


}
