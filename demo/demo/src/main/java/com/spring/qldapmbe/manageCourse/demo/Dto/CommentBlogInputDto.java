package com.spring.qldapmbe.manageCourse.demo.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentBlogInputDto {
	private String content;
	private Integer parent_id;
}
