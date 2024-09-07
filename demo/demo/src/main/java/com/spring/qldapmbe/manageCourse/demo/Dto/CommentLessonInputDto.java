package com.spring.qldapmbe.manageCourse.demo.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentLessonInputDto {
	private String content;
	private Integer parentCommentId;
}
