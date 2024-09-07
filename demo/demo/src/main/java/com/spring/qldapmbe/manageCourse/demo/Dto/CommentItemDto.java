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
public class CommentItemDto {
	private Integer _id;
	private String content;
	private UserCreatorDto creator;
	private Integer parent_id;
	private Date createdAt;
	private Date updatedAt;
}
