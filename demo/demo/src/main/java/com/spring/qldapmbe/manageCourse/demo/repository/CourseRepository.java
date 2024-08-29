package com.spring.qldapmbe.manageCourse.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.qldapmbe.manageCourse.demo.entity.Course;


@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

}
