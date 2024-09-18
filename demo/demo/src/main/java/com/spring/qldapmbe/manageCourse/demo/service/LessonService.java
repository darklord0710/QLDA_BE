package com.spring.qldapmbe.manageCourse.demo.service;

import java.util.List;

import com.spring.qldapmbe.manageCourse.demo.entity.Course;
import com.spring.qldapmbe.manageCourse.demo.entity.Lesson;

public interface LessonService {

	void saveLesson(Lesson lesson);

	void setCloudinaryField(Lesson lesson);

	Lesson findLessonById(Integer id);

	List<Lesson> findByCourse(Course course);

}
