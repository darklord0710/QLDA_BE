package com.spring.qldapmbe.manageCourse.demo.Dto;

import com.qldapm.spring.eCourseWeb.entity.Comment;
import com.qldapm.spring.eCourseWeb.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentBlogOutputDto {

	private Comment comment;
	private User user;
}
