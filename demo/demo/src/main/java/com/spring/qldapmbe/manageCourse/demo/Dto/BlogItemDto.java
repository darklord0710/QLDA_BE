package com.spring.qldapmbe.manageCourse.demo.Dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BlogItemDto {
	private List<BlogDtoOutputById> blogs;
	private Integer blogCount;
}
