package com.spring.qldapmbe.manageCourse.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.qldapmbe.manageCourse.demo.entity.Course;
import com.spring.qldapmbe.manageCourse.demo.entity.Lesson;


@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
	List<Lesson> findByCourse(Course course);
}
