package com.spring.qldapmbe.manageCourse.demo.controller;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.spring.qldapmbe.manageCourse.demo.entity.Course;
import com.spring.qldapmbe.manageCourse.demo.entity.Lesson;
import com.spring.qldapmbe.manageCourse.demo.entity.User;
import com.spring.qldapmbe.manageCourse.demo.service.CourseService;
import com.spring.qldapmbe.manageCourse.demo.service.LessonService;
import com.spring.qldapmbe.manageCourse.demo.service.UserCourseService;
import com.spring.qldapmbe.manageCourse.demo.service.UserService;



@Controller
public class AdminController {

	private UserService userService;
	private CourseService courseService;
	private LessonService lessonService;
	private UserCourseService userCourseService;

	@Autowired
	public AdminController(UserService userService, CourseService courseService,
			LessonService lessonService, UserCourseService userCourseService) {
		super();
		this.userService = userService;
		this.courseService = courseService;
		this.lessonService = lessonService;
		this.userCourseService = userCourseService;
	}

	@ModelAttribute
	public void addAttributes(Model model) {
		User user = userService.getCurrentLoginUser();
		model.addAttribute("currentUser", user);

		List<Course> lsc = courseService.findAllCourses();
		model.addAttribute("lsc", lsc);

	}

	@GetMapping("/admin")
	public String index() {
		return "admin/index";
	}

	@GetMapping("/admin/addNewCourse")
	public String getFormAddNewCourse(Model model) {
		model.addAttribute("course", new Course());
		return "admin/addOrUpdateCourse";
	}

	@GetMapping("/admin/updateCourse/{courseId}")
	public String getFormUpdateCourse(Model model, @PathVariable("courseId") Integer courseId) {
		Course course = courseService.findCourseById(courseId);
		model.addAttribute("course", course);
		return "admin/addOrUpdateCourse";
	}

	@PostMapping("/admin/addOrUpdateCourse")
	public String addOrUpdateCourse(Model model, @ModelAttribute("course") Course course,
			@RequestParam Map<String, String> params,
			@RequestPart("imageFile") MultipartFile imageFile) throws ParseException {

		String isActived = params.getOrDefault("isActived", "false");
		if (isActived != null && isActived.equals("true"))
			course.setIsActived(true);
		else
			course.setIsActived(false);

		course.setUpdatedDate(new Date());

		if (course.getId() != null && course.getId() > 0) {
			Course tempCourse = courseService.findCourseById(course.getId());
			course.setCreatedDate(tempCourse.getCreatedDate());
			courseService.saveCourse(course);

			if (imageFile.getOriginalFilename() != ""
					|| !imageFile.getOriginalFilename().isEmpty()) {
				course.setFile(imageFile);
				courseService.setCloudinaryField(course);
			}

			return "redirect:/admin/coursesList";
		}

		course.setCreatedDate(new Date());
		course.setFile(imageFile);
		courseService.setCloudinaryField(course);

		return "redirect:/admin/coursesList";
	}

	@GetMapping("/admin/coursesList")
	public String getFormCoursesList() {

		return "admin/coursesList";
	}

	@GetMapping("/admin/addNewLesson")
	public String getFormAddNewLesson(Model model) {

		Lesson lesson = new Lesson();
		model.addAttribute("lesson", lesson);

		return "admin/addOrUpdateLesson";
	}

	@GetMapping("/admin/updateLesson/{lessonId}")
	public String getFormUpdateLesson(Model model, @PathVariable("lessonId") Integer lessonId) {

		Lesson lesson = lessonService.findLessonById(lessonId);
		model.addAttribute("lesson", lesson);

		return "admin/addOrUpdateLesson";
	}

	@PostMapping("/admin/addOrUpdateLesson")
	public String addOrUpdateLesson(Model model, @ModelAttribute("lesson") Lesson lesson,
			@RequestParam Map<String, String> params,
			@RequestPart("videoFile") MultipartFile videoFile) {

		String isActived = params.get("isActived");
		Integer courseId = Integer.parseInt(params.get("selectCourse"));

		Course course = courseService.findCourseById(courseId);
		lesson.setCourse(course);

		if (isActived == null)
			lesson.setIsActived(false);
		else if (isActived.equals("true") && isActived != null)
			lesson.setIsActived(true);
		lesson.setUpdatedDate(new Date());

		if (lesson.getId() != null && lesson.getId() > 0) {
			Lesson tempLesson = lessonService.findLessonById(lesson.getId());
			lesson.setCreatedDate(tempLesson.getCreatedDate());
			lessonService.saveLesson(lesson);

			if (videoFile.getOriginalFilename() != ""
					|| !videoFile.getOriginalFilename().isEmpty()) {
				lesson.setFile(videoFile);
				lessonService.setCloudinaryField(lesson);
			}

			return "redirect:/admin/coursesList";

		}

		lesson.setCreatedDate(new Date());
		lesson.setFile(videoFile);
		lessonService.setCloudinaryField(lesson);

		return "redirect:/admin/coursesList";
	}

	@GetMapping("/admin/stats")
	public String getFormStats(Model model, @RequestParam Map<String, String> params) {
		String monthlyCourseSold = params.getOrDefault("monthlyCourseSold",
				String.valueOf(LocalDateTime.now().getYear()));

		monthlyCourseSold = monthlyCourseSold.isEmpty() || monthlyCourseSold.equals("")
				? String.valueOf(LocalDateTime.now().getYear())
				: monthlyCourseSold;

		List<Object[]> stats1 = userCourseService
				.statsMonlyCourseSoldByYear(Integer.parseInt(monthlyCourseSold));

		List<Object[]> stats2 = userCourseService.statsRevenueYearlyCourseSoldAllYear();

		List<Map<String, Object>> formattedResults = new ArrayList<>();
		List<Map<String, Object>> formattedResults2 = new ArrayList<>();

		for (Object[] s : stats1) {
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("monthJoined", s[0]);
			resultMap.put("quantity", s[1]);
			formattedResults.add(resultMap);
		}

		for (Object[] s : stats2) {
			Map<String, Object> resultMap2 = new HashMap<>();
			resultMap2.put("yearJoined", s[0]);
			resultMap2.put("quantity", s[1]);
			formattedResults2.add(resultMap2);
		}

		model.addAttribute("yearCourseSold", monthlyCourseSold);
		model.addAttribute("monthlyJoinedCount", formattedResults);
		model.addAttribute("yearlyRevenueCount", formattedResults2);
		return "/admin/stats";
	}



}
