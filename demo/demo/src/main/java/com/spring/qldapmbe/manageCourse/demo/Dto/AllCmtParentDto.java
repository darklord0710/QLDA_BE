package com.spring.qldapmbe.manageCourse.demo.Dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllCmtParentDto {
	private List<CommentItemDto> parent_comments;
	private Integer total_comments;
}
