package com.spring.qldapmbe.manageCourse.demo.service.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

	@Override
	public Page<Course> paginatedCourse(Integer page, Integer size, List<Course> courses) {

		Pageable pageable = PageRequest.of(page - 1, size);

		int start = (int) pageable.getOffset();
		int end = 0;
		List<Course> coursesPaginated;

		if (courses.size() < start) {
			coursesPaginated = Collections.emptyList();
		} else {
			end = Math.min((start + pageable.getPageSize()), courses.size());
			coursesPaginated = courses.subList(start, end);
		}

		return new PageImpl<>(coursesPaginated, pageable, courses.size());
	}

}
