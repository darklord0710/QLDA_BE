package com.spring.qldapmbe.manageCourse.demo.Dto;



import com.spring.qldapmbe.manageCourse.demo.entity.Blog;
import com.spring.qldapmbe.manageCourse.demo.entity.User;

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
