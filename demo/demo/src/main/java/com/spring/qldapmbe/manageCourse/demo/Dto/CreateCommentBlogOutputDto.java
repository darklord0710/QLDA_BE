package com.spring.qldapmbe.manageCourse.demo.Dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentBlogOutputDto {
	private String content;
	private Integer creator;
	private Integer parent_id;
	private Integer _id;
	private Date createdAt;
	private Date updatedAt;
}
