package com.spring.qldapmbe.manageCourse.demo.service;

import java.util.List;

import com.spring.qldapmbe.manageCourse.demo.entity.Course;


public interface CourseService {

	void saveCourse(Course course);

	void setCloudinaryField(Course course);

	List<Course> findAllCourses();

	Course findCourseById(Integer courseId);
}
