package com.spring.qldapmbe.manageCourse.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.qldapmbe.manageCourse.demo.entity.CommentLesson;


@Repository
public interface CommentLessonRepository extends JpaRepository<CommentLesson, Integer> {

}
