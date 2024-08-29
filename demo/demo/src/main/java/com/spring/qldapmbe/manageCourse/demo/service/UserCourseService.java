package com.spring.qldapmbe.manageCourse.demo.service;

import java.util.List;

import com.spring.qldapmbe.manageCourse.demo.entity.Course;
import com.spring.qldapmbe.manageCourse.demo.entity.User;
import com.spring.qldapmbe.manageCourse.demo.entity.UserCourse;



public interface UserCourseService {

	void saveUserCourse(UserCourse userCourse);

	UserCourse findUserCourseByUserAndCourse(User user, Course course);

	List<UserCourse> findUserCourseByUser(User user);

	List<Object[]> statsMonlyCourseSoldByYear(Integer year);

	List<Object[]> statsRevenueYearlyCourseSoldAllYear();

}
