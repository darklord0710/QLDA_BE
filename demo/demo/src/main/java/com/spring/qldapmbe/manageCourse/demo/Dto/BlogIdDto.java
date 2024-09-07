package com.spring.qldapmbe.manageCourse.demo.Dto;

import com.qldapm.spring.eCourseWeb.entity.Blog;
import com.qldapm.spring.eCourseWeb.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogIdDto {
	private User user;
	private Blog blog;
}
