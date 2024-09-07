package com.spring.qldapmbe.manageCourse.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.qldapmbe.manageCourse.demo.entity.Blog;
import com.spring.qldapmbe.manageCourse.demo.entity.User;




@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {

	@Query("SELECT b FROM Blog b WHERE b.user = :user")
	List<Blog> findAllBlogsByUser(@Param("user") User user);

	@Query("SELECT COUNT(b) FROM Blog b WHERE b.user = :user")
	Integer countBlogByCurrentUser(@Param("user") User user);
}
