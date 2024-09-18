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
import com.spring.qldapmbe.manageCourse.demo.entity.Lesson;
import com.spring.qldapmbe.manageCourse.demo.repository.LessonRepository;
import com.spring.qldapmbe.manageCourse.demo.service.LessonService;

import jakarta.transaction.Transactional;

@Service
public class LessonServiceImpl implements LessonService {

	@Autowired
	private LessonRepository lessonRepository;
	@Autowired
	private Cloudinary cloudinary;

	@Override
	@Transactional
	public void saveLesson(Lesson lesson) {
		lessonRepository.save(lesson);
	}

	@Override
	public void setCloudinaryField(Lesson lesson) {
		if (!lesson.getFile().isEmpty()) {
			try {
				Map res = this.cloudinary.uploader().upload(lesson.getFile().getBytes(),
						ObjectUtils.asMap("resource_type", "auto"));
				lesson.setVideoUrl(res.get("secure_url").toString());
				lesson.setFile(null);
				this.lessonRepository.save(lesson);

			} catch (IOException ex) {
				Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	@Override
	public Lesson findLessonById(Integer id) {
		Optional<Lesson> lesson = lessonRepository.findById(id);
		if (!lesson.isPresent())
			return null;

		Lesson l = lesson.get();
		return l;
	}

	@Override
	public List<Lesson> findByCourse(Course course) {
		return lessonRepository.findByCourse(course);
	}

}
