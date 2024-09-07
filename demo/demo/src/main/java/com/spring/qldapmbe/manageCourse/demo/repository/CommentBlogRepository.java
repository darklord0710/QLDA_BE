package com.spring.qldapmbe.manageCourse.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.qldapmbe.manageCourse.demo.entity.Blog;
import com.spring.qldapmbe.manageCourse.demo.entity.CommentBlog;



@Repository
public interface CommentBlogRepository extends JpaRepository<CommentBlog, Integer> {

	@Query("SELECT cb FROM CommentBlog cb where cb.blog = :blog AND cb.comment.comment IS NULL")
	List<CommentBlog> findAllParentCommentBlog(@Param("blog") Blog blog);

	@Query("SELECT cb FROM CommentBlog cb where cb.blog = :blog AND cb.comment.comment.id = :parentId ")
	List<CommentBlog> findAllChildCommentBlogByParentId(@Param("blog") Blog blog,
			@Param("parentId") Integer parentId);

	@Query("SELECT COUNT(cb) FROM CommentBlog cb where cb.blog = :blog ")
	Integer countCommentBlogByBlog(@Param("blog") Blog blog);

	@Query("SELECT COUNT(cb) FROM CommentBlog cb where cb.blog = :blog AND cb.comment.comment.id = :parentId  ")
	Integer countChildCommentBlogByParentId(@Param("blog") Blog blog,
			@Param("parentId") Integer parentId);
}
