package com.spring.qldapmbe.manageCourse.demo.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatorDto {
	private Integer _id;
	private String name;
	private String avatar;
	private String desc;
	private String slug;
}
