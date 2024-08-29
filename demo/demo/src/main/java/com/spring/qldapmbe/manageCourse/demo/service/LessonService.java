package com.spring.qldapmbe.manageCourse.demo.service;

import com.spring.qldapmbe.manageCourse.demo.entity.Lesson;

public interface LessonService {

	void saveLesson(Lesson lesson);

	void setCloudinaryField(Lesson lesson);

	Lesson findLessonById(Integer id);

}
