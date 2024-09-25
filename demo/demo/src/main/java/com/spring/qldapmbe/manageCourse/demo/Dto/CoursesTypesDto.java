package com.spring.qldapmbe.manageCourse.demo.Dto;

import java.util.List;

import com.spring.qldapmbe.manageCourse.demo.entity.Course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoursesTypesDto {
	private List<Course> freeCourses;
	private List<Course> proCourses;
}
