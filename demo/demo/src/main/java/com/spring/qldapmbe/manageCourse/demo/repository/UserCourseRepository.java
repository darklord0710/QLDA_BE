package com.spring.qldapmbe.manageCourse.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.qldapmbe.manageCourse.demo.entity.Course;
import com.spring.qldapmbe.manageCourse.demo.entity.User;
import com.spring.qldapmbe.manageCourse.demo.entity.UserCourse;



@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Integer> {

	@Query("SELECT uc FROM UserCourse uc WHERE uc.user = :user and uc.course = :course")
	UserCourse findUserCourseByUserAndCourse(@Param("user") User user,
			@Param("course") Course course);

	List<UserCourse> findByUser(User user);

	@Query("SELECT MONTH(uc.joinedDate) as monthJoined, COUNT(uc.id) as quantity " +
			"FROM UserCourse uc " +
			"LEFT JOIN uc.course c " +
			"WHERE YEAR(uc.joinedDate) = :year " +
			"GROUP BY MONTH(uc.joinedDate) " +
			"ORDER BY MONTH(uc.joinedDate)")
	List<Object[]> statsMonlyCourseSoldByYear(@Param("year") Integer year);

	@Query("SELECT YEAR(uc.joinedDate) as yearJoined , SUM(c.price) as Revenue " +
			"FROM UserCourse uc " +
			"LEFT JOIN uc.course c " +
			"WHERE c.price > 0 " +
			"GROUP BY YEAR(uc.joinedDate) ")
	List<Object[]> statsRevenueYearlyCourseSoldAllYear();
}
