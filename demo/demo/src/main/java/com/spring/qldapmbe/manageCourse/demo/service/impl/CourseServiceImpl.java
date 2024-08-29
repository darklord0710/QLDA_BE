package com.spring.qldapmbe.manageCourse.demo.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.spring.qldapmbe.manageCourse.demo.entity.Course;
import com.spring.qldapmbe.manageCourse.demo.repository.CourseRepository;
import com.spring.qldapmbe.manageCourse.demo.service.CourseService;

import jakarta.transaction.Transactional;

@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private Cloudinary cloudinary;

	@Override
	@Transactional
	public void saveCourse(Course course) {
		courseRepository.save(course);
	}

	@Override
	public void setCloudinaryField(Course course) {
		if (!course.getFile().isEmpty()) {
			try {
				Map res = this.cloudinary.uploader().upload(course.getFile().getBytes(),
						ObjectUtils.asMap("resource_type", "auto"));
				course.setImageUrl(res.get("secure_url").toString());
				course.setFile(null);
				this.courseRepository.save(course);

			} catch (IOException ex) {
				Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	@Override
	public List<Course> findAllCourses() {
		return courseRepository.findAll();
	}

	@Override
	public Course findCourseById(Integer courseId) {
		Optional<Course> course = courseRepository.findById(courseId);
		if (!course.isPresent())
			return null;

		Course c = course.get();

		return c;
	}

}
