package com.spring.qldapmbe.manageCourse.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.qldapmbe.manageCourse.demo.entity.Course;
import com.spring.qldapmbe.manageCourse.demo.entity.User;
import com.spring.qldapmbe.manageCourse.demo.entity.UserCourse;
import com.spring.qldapmbe.manageCourse.demo.repository.UserCourseRepository;
import com.spring.qldapmbe.manageCourse.demo.service.UserCourseService;

import jakarta.transaction.Transactional;

@Service
public class UserCourseSeriveImpl implements UserCourseService {

	@Autowired
	private UserCourseRepository userCourseRepository;

	@Override
	@Transactional
	public void saveUserCourse(UserCourse userCourse) {
		userCourseRepository.save(userCourse);
	}

	@Override
	public UserCourse findUserCourseByUserAndCourse(User user, Course course) {
		return userCourseRepository.findUserCourseByUserAndCourse(user, course);
	}

	@Override
	public List<UserCourse> findUserCourseByUser(User user) {
		return userCourseRepository.findByUser(user);
	}

	@Override
	public List<Object[]> statsMonlyCourseSoldByYear(Integer year) {
		return userCourseRepository.statsMonlyCourseSoldByYear(year);
	}

	@Override
	public List<Object[]> statsRevenueYearlyCourseSoldAllYear() {
		return userCourseRepository.statsRevenueYearlyCourseSoldAllYear();
	}

}
