package com.spring.qldapmbe.manageCourse.demo.Dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlogDtoOutputById {
	private Integer _id;
	private String title;
	private String desc;
	private String content;
	private UserCreatorDto creator;
	private Date createdAt;
	private Date updatedAt;
}
