package com.spring.qldapmbe.manageCourse.demo.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.qldapmbe.manageCourse.demo.Dto.AllCmtParentDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.BlogDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.BlogDtoOutputById;
import com.spring.qldapmbe.manageCourse.demo.Dto.BlogItemDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.CommentBlogInputDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.CommentItemDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.CoursesTypesDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.CreateCommentBlogOutputDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.EmailDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.HasLikedDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.LessonQuickViewDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.MessageDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.PasswordChangeDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.TotalLikeDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.UpdateContentCmtDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.UserCreatorDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.UserLoginDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.UserRegisterDto;
import com.spring.qldapmbe.manageCourse.demo.config.JwtService;
import com.spring.qldapmbe.manageCourse.demo.entity.Blog;
import com.spring.qldapmbe.manageCourse.demo.entity.Comment;
import com.spring.qldapmbe.manageCourse.demo.entity.CommentBlog;
import com.spring.qldapmbe.manageCourse.demo.entity.Course;
import com.spring.qldapmbe.manageCourse.demo.entity.Lesson;
import com.spring.qldapmbe.manageCourse.demo.entity.LikeBlog;
import com.spring.qldapmbe.manageCourse.demo.entity.LikeComment;
import com.spring.qldapmbe.manageCourse.demo.entity.User;
import com.spring.qldapmbe.manageCourse.demo.entity.UserCourse;
import com.spring.qldapmbe.manageCourse.demo.entity.VerifyEmail;
import com.spring.qldapmbe.manageCourse.demo.service.BlogService;
import com.spring.qldapmbe.manageCourse.demo.service.CommentBlogService;
import com.spring.qldapmbe.manageCourse.demo.service.CommentLessonService;
import com.spring.qldapmbe.manageCourse.demo.service.CommentService;
import com.spring.qldapmbe.manageCourse.demo.service.CourseService;
import com.spring.qldapmbe.manageCourse.demo.service.LessonService;
import com.spring.qldapmbe.manageCourse.demo.service.LikeBlogService;
import com.spring.qldapmbe.manageCourse.demo.service.LikeCommentService;
import com.spring.qldapmbe.manageCourse.demo.service.MailSenderService;
import com.spring.qldapmbe.manageCourse.demo.service.UserCourseService;
import com.spring.qldapmbe.manageCourse.demo.service.UserService;
import com.spring.qldapmbe.manageCourse.demo.service.VerifyEmailService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/users")
public class ApiUserRestController {

	private JwtService jwtService;
	private UserService userService;
	private MailSenderService mailSenderService;
	private VerifyEmailService verifyEmailService;
	private BlogService blogService;
	private LikeBlogService likeBlogService;
	private CommentService commentService;
	private CommentBlogService commentBlogService;
	private LikeCommentService likeCommentService;
	private LessonService lessonService;
	private CommentLessonService commentLessonService;
	private CourseService courseService;
	private UserCourseService userCourseService;
	private PasswordEncoder encoder;

	@Autowired
	public ApiUserRestController(JwtService jwtService, UserService userService,
			MailSenderService mailSenderService, VerifyEmailService verifyEmailService,
			BlogService blogService, LikeBlogService likeBlogService,
			CommentService commentService, CommentBlogService commentBlogService,
			LikeCommentService likeCommentService, LessonService lessonService,
			CommentLessonService commentLessonService, CourseService courseService,
			UserCourseService userCourseService, PasswordEncoder encoder) {
		super();
		this.jwtService = jwtService;
		this.userService = userService;
		this.mailSenderService = mailSenderService;
		this.verifyEmailService = verifyEmailService;
		this.blogService = blogService;
		this.likeBlogService = likeBlogService;
		this.commentService = commentService;
		this.commentBlogService = commentBlogService;
		this.likeCommentService = likeCommentService;
		this.lessonService = lessonService;
		this.commentLessonService = commentLessonService;
		this.courseService = courseService;
		this.userCourseService = userCourseService;
		this.encoder = encoder;
	}

	@PostMapping(path = "/auth/login/")
	@CrossOrigin
	public ResponseEntity<Object> login(@RequestBody UserLoginDto loginDto) {

		if (!userService.authUser(loginDto.getEmail(), loginDto.getPassword()))
			return new ResponseEntity<>(new MessageDto("Invalid email or password"),
					HttpStatus.UNAUTHORIZED);

		String token = jwtService.generateTokenLogin(loginDto.getEmail(),
				loginDto.getPassword());

		return new ResponseEntity<>(token, HttpStatus.OK);

	}

	@GetMapping(path = "/current-user/")
	@CrossOrigin
	public ResponseEntity<Object> getCurrentUserApi() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			User user = userService.findByEmail((authentication.getName()));
			return new ResponseEntity<>(user, HttpStatus.OK);
		}
		return new ResponseEntity<>(new MessageDto("Invalid token"),
				HttpStatus.UNAUTHORIZED);
	}

	@GetMapping(path = "/{slug}/")
	@CrossOrigin
	public ResponseEntity<Object> getUserBySlug(@PathVariable("slug") String slug) {
		User user = userService.findBySlug(slug);
		if (user == null)
			return new ResponseEntity<>(new MessageDto("Invalid user"),
					HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(user,
				HttpStatus.OK);
	}

	@PostMapping(path = "/auth/check-email/")
	@CrossOrigin
	public ResponseEntity<Object> checkExistEmail(@RequestBody EmailDto emailDto)
			throws UnsupportedEncodingException, MessagingException {

		User existUser = userService.findByEmail(emailDto.getEmail());
		if (existUser != null)
			return new ResponseEntity<>(new MessageDto("This email is existed !"),
					HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>("Successfully !", HttpStatus.OK);
	}

	@PostMapping(path = "/auth/verify-email/")
	@CrossOrigin
	public ResponseEntity<Object> retrieveOtp(@RequestBody EmailDto emailDto)
			throws UnsupportedEncodingException, MessagingException {

		User existUser = userService.findByEmail(emailDto.getEmail());
		if (existUser != null)
			return new ResponseEntity<>(new MessageDto("User is not existed"),
					HttpStatus.BAD_REQUEST);

		mailSenderService.sendOtpEmail(emailDto.getEmail());

		return new ResponseEntity<>(new MessageDto("Sent mail successfully"), HttpStatus.OK);
	}

	@PostMapping(path = "/auth/register/")
	@CrossOrigin
	public ResponseEntity<Object> register(@RequestBody UserRegisterDto registerDto) {

		User existUser = userService.findByEmail(registerDto.getEmail());
		VerifyEmail verifyEmail = verifyEmailService.findByEmail(registerDto.getEmail());

		if (existUser != null || verifyEmail == null)
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (verifyEmailService.isOtpExpiredTime(verifyEmail))
			return new ResponseEntity<>(new MessageDto("Invalid OTP or has expired"),
					HttpStatus.UNAUTHORIZED);

		if (!verifyEmailService.isOtpMatched(registerDto.getOtp(), verifyEmail))
			return new ResponseEntity<>(new MessageDto("Invalid OTP or has expired"),
					HttpStatus.UNAUTHORIZED);

		userService.saveUserRegisterDto(registerDto);
		existUser = userService.findByEmail(registerDto.getEmail());

		return new ResponseEntity<>(existUser, HttpStatus.CREATED);

	}

	@PostMapping("/auth/change-password/")
	@CrossOrigin
	public ResponseEntity<Object> changePassword(@RequestBody PasswordChangeDto passwordChangeDto) {
		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<Object>(new MessageDto("Lỗi chưa đăng nhập !"),
					HttpStatus.NOT_FOUND);

		if (!encoder.matches(passwordChangeDto.getCurrentPassword(), currentUser.getPassword()))
			return new ResponseEntity<Object>(new MessageDto("Sai mật khẩu hiện tại"),
					HttpStatus.UNAUTHORIZED);

		if (encoder.matches(passwordChangeDto.getNewPassword(), currentUser.getPassword()))
			return new ResponseEntity<Object>(new MessageDto("Trùng mật khẩu cũ"),
					HttpStatus.UNAUTHORIZED);

		currentUser.setPassword(encoder.encode(passwordChangeDto.getNewPassword()));
		userService.saveUser(currentUser);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PatchMapping(path = "/current-user/", consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@CrossOrigin
	public ResponseEntity<Object> updatedAvatar(
			@RequestPart(name = "avatar", required = false) MultipartFile file,
			@RequestParam Map<String, String> params) {

		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<Object>(new MessageDto("Lỗi chưa đăng nhập !"),
					HttpStatus.NOT_FOUND);

		String slug = params.getOrDefault("slug", null);
		if (slug != null && !slug.isBlank()) {

			User existSlugUser = userService.findBySlug(slug);
			if (existSlugUser != null && !existSlugUser.getId().equals(currentUser.getId()))
				return new ResponseEntity<Object>(new MessageDto("Trùng slug !"),
						HttpStatus.UNAUTHORIZED);

			currentUser.setSlug(slug);
		}

		String name = params.getOrDefault("name", null);
		if (name != null && !name.isBlank())
			currentUser.setName(name);

		String desc = params.getOrDefault("desc", null);
		if (desc != null && !desc.isBlank())
			currentUser.setDesc(desc);

		if (file != null && !file.isEmpty()) {
			currentUser.setFile(file);
			userService.setCloudinaryField(currentUser);
		}

		userService.saveUser(currentUser);

		return new ResponseEntity<>(currentUser, HttpStatus.OK);
	}

	@GetMapping(path = "/blogs/")
	@CrossOrigin
	public ResponseEntity<Object> getAllBlogs(@RequestParam Map<String, String> params) {

		Integer page = Integer.parseInt(params.getOrDefault("page", "1"));
		Integer size = Integer.parseInt(params.getOrDefault("size", "10"));

		List<Blog> blogs = blogService.findAllBlogs();
		List<BlogDtoOutputById> data = new ArrayList<>();

		blogs.stream().forEach(blog -> {
			data.add(new BlogDtoOutputById(blog.getId(), blog.getTitle(),
					blog.getDesc(), blog.getContent(),
					new UserCreatorDto(blog.getUser().getId(), blog.getUser().getName(),
							blog.getUser().getAvatar(), blog.getUser().getDesc(),
							blog.getUser().getSlug()),
					blog.getCreatedDate(),
					blog.getUpdatedDate()));
		});

		Page<BlogDtoOutputById> allBlogsPaginated = blogService
				.allBlogsPaginated(page, size, data);

		return new ResponseEntity<Object>(allBlogsPaginated, HttpStatus.OK);
	}

	@PostMapping(path = "/my-blogs/")
	@CrossOrigin
	public ResponseEntity<Blog> createBlog(@RequestBody BlogDto blogDto) {

		User user = userService.getCurrentLoginUser();

		Blog blog = new Blog();
		blog.setTitle(blogDto.getTitle());
		blog.setContent(blogDto.getContent());
		blog.setUser(user);
		blog.setCreatedDate(new Date());
		blogService.saveBlog(blog);

		return new ResponseEntity<>(blog, HttpStatus.CREATED);
	}

	@GetMapping(path = "/my-blogs/")
	@CrossOrigin
	public ResponseEntity<Object> getAllBlogsByCurrentUser() {

		User currentUser = userService.getCurrentLoginUser();

		if (currentUser == null)
			return new ResponseEntity<Object>("Lỗi chưa đăng nhập !", HttpStatus.NOT_FOUND);

		List<BlogDtoOutputById> blogs = new ArrayList<>();
		Integer blogCount = blogService.countBlogByCurrentUser(currentUser);
		currentUser.getBlogs().stream().forEach(blog -> {

			blogs.add(new BlogDtoOutputById(blog.getId(), blog.getTitle(),
					blog.getDesc(), blog.getContent(),
					new UserCreatorDto(currentUser.getId(), currentUser.getName(),
							currentUser.getAvatar(), currentUser.getDesc(), currentUser.getSlug()),
					blog.getCreatedDate(),
					blog.getUpdatedDate()));

		});

		BlogItemDto blogItemDto = new BlogItemDto(blogs, blogCount);

		return new ResponseEntity<>(blogItemDto, HttpStatus.OK);
	}

	@GetMapping(path = "/blogs/{blogId}/")
	@CrossOrigin
	public ResponseEntity<Object> getBlog(@PathVariable("blogId") Integer blogId) {

		User currentUser = userService.getCurrentLoginUser();

		Blog blog = blogService.findById(blogId);

		if (blog == null || currentUser == null || blog.getUser().getId() != currentUser.getId())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		BlogDtoOutputById blogDtoOutputById = new BlogDtoOutputById(blog.getId(), blog.getTitle(),
				blog.getDesc(), blog.getContent(),
				new UserCreatorDto(currentUser.getId(), currentUser.getName(),
						currentUser.getAvatar(), currentUser.getDesc(), currentUser.getSlug()),
				blog.getCreatedDate(),
				blog.getUpdatedDate());

		return new ResponseEntity<>(blogDtoOutputById, HttpStatus.OK);
	}

	@GetMapping(path = "/blogs/{blogId}/likes/")
	@CrossOrigin
	public ResponseEntity<Object> countLike(@PathVariable("blogId") Integer blogId) {

		Blog blog = blogService.findById(blogId);
		if (blog == null)
			return new ResponseEntity<>("Không tìm thấy bài viết này ", HttpStatus.NOT_FOUND);
		Integer totalLike = likeBlogService.countLikeBlogByBlog(blog);

		return new ResponseEntity<>(new TotalLikeDto(totalLike), HttpStatus.OK);
	}

	@PatchMapping(path = "/blogs/{blogId}/")
	@CrossOrigin
	public ResponseEntity<Object> updateBlog(@PathVariable("blogId") Integer blogId,
			@RequestBody BlogDto blogDto) {

		User currentUser = userService.getCurrentLoginUser();

		Blog blog = blogService.findById(blogId);

		if (blog == null || currentUser == null || blog.getUser().getId() != currentUser.getId())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		blog.setContent(blogDto.getContent());
		blog.setDesc(blogDto.getDesc());
		blog.setTitle(blogDto.getTitle());

		blogService.saveBlog(blog);

		return new ResponseEntity<>(blog, HttpStatus.OK);
	}

	@GetMapping(path = "/blogs/{blogId}/likes/checked/")
	@CrossOrigin
	public ResponseEntity<Object> checkedLikeBlog(@PathVariable("blogId") Integer blogId) {
		User currentUser = userService.getCurrentLoginUser();
		Blog blog = blogService.findById(blogId);

		if (blog == null || currentUser == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		LikeBlog likeBlog = likeBlogService.findLikeBlogByUserAndBlog(currentUser, blog);

		Boolean hasLike = likeBlog == null ? false : likeBlog.getHasLiked();

		return new ResponseEntity<>(new HasLikedDto(hasLike), HttpStatus.OK);
	}

	@PostMapping(path = "/blogs/{blogId}/likes/")
	@CrossOrigin
	@Async
	public ResponseEntity<LikeBlog> toggleLikeBlog(@PathVariable("blogId") Integer blogId) {

		User currentUser = userService.getCurrentLoginUser();
		Blog blog = blogService.findById(blogId);

		if (blog == null || currentUser == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		LikeBlog likeBlog = likeBlogService.findLikeBlogByUserAndBlog(currentUser, blog);
		if (likeBlog != null) {
			likeBlog.setHasLiked(!likeBlog.getHasLiked());
			likeBlogService.saveLikeBlog(likeBlog);

			likeBlog.setBlog(blog);
			likeBlogService.saveLikeBlog(likeBlog);

			return new ResponseEntity<>(likeBlog, HttpStatus.OK);
		}

		likeBlog = new LikeBlog();
		likeBlog.setBlog(blog);
		likeBlog.setUser(currentUser);
		likeBlog.setHasLiked(true);
		likeBlogService.saveLikeBlog(likeBlog);

		return new ResponseEntity<>(likeBlog, HttpStatus.CREATED);
	}

	@PostMapping(path = "/blogs/{blogId}/comments/")
	@CrossOrigin
	public ResponseEntity<Object> commentBlog(@PathVariable("blogId") Integer blogId,
			@RequestBody CommentBlogInputDto cbi) {

		Integer parentId = cbi.getParent_id();

		User currentUser = userService.getCurrentLoginUser();
		Blog blog = blogService.findById(blogId);

		if (blog == null || currentUser == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		Comment comment = new Comment();
		comment.setCreatedDate(new Date());
		comment.setContent(cbi.getContent());
		comment.setUser(currentUser);

		if (parentId != null) {
			Comment parentCom = commentService.findCommentById(parentId);
			comment.setComment(parentCom);
		}

		commentService.saveComment(comment); // ko cần save cx đc vì đã save object chứa object khóa
												// ngoại

		CommentBlog commentBlog = new CommentBlog();
		commentBlog.setBlog(blog);
		commentBlog.setComment(comment);

		commentBlogService.saveCommentBlog(commentBlog);

		CreateCommentBlogOutputDto c = new CreateCommentBlogOutputDto(comment.getContent(),
				currentUser.getId(), parentId, comment.getId(),
				comment.getCreatedDate(), comment.getUpdatedDate());

		return new ResponseEntity<>(c, HttpStatus.CREATED);
	}

	@PatchMapping(path = "/comments/{commentId}/")
	@CrossOrigin
	public ResponseEntity<Object> updateComment(@PathVariable("commentId") Integer commentId,
			@RequestBody UpdateContentCmtDto uccDto) {

		User currentUser = userService.getCurrentLoginUser();
		Comment comment = commentService.findCommentById(commentId);

		if (comment == null || currentUser == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		comment.setContent(uccDto.getContent());

		commentService.saveComment(comment);

		Integer parentId = comment.getComment() == null ? null : comment.getComment().getId();

		CreateCommentBlogOutputDto c = new CreateCommentBlogOutputDto(comment.getContent(),
				currentUser.getId(), parentId, comment.getId(),
				comment.getCreatedDate(), comment.getUpdatedDate());

		return new ResponseEntity<>(c, HttpStatus.OK);
	}

	@DeleteMapping(path = "/comments/{commentId}/")
	@CrossOrigin
	public ResponseEntity<Object> deleteComment(@PathVariable("commentId") Integer commentId) {

		User currentUser = userService.getCurrentLoginUser();
		Comment comment = commentService.findCommentById(commentId);

		if (comment == null || currentUser == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		if (!currentUser.equals(comment.getUser()))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		commentService.deleteComment(comment);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping(path = "/blogs/{blogId}/comments/")
	@CrossOrigin
	public ResponseEntity<Object> getParentCommentsBlog(
			@PathVariable("blogId") Integer blogId) {

		Blog blog = blogService.findById(blogId);

		if (blog == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		List<CommentBlog> cmbs = commentBlogService.findAllParentCommentBlog(blog);
		List<CommentItemDto> ciDtos = new ArrayList<>();

		cmbs.stream().forEach(cmb -> {
			Integer parentId = cmb.getComment().getComment() == null ? null
					: cmb.getComment().getComment().getId();

			ciDtos.add(new CommentItemDto(cmb.getComment().getId(), cmb.getComment().getContent(),
					new UserCreatorDto(cmb.getComment().getUser().getId(),
							cmb.getComment().getUser().getName(),
							cmb.getComment().getUser().getAvatar(),
							cmb.getComment().getUser().getDesc(),
							cmb.getComment().getUser().getSlug()),
					parentId, cmb.getComment().getCreatedDate(),
					cmb.getComment().getUpdatedDate()));
		});

		Integer total_comments = commentBlogService.countCommentBlogByBlog(blog);
		return new ResponseEntity<>(new AllCmtParentDto(ciDtos, total_comments), HttpStatus.OK);
	}

	@GetMapping(path = "/blogs/{blogId}/comments/{parentCommentId}/")
	@CrossOrigin
	public ResponseEntity<Object> getChildCommentByParentCommentId(
			@PathVariable("blogId") Integer blogId,
			@PathVariable("parentCommentId") Integer parentCommentId) {

		Blog blog = blogService.findById(blogId);
		if (blog == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		List<CommentBlog> cmbs = commentBlogService.findAllChildCommentBlogByParentId(blog,
				parentCommentId);

		List<CommentItemDto> ciDtos = new ArrayList<>();

		cmbs.stream().forEach(cmb -> {

			ciDtos.add(new CommentItemDto(cmb.getComment().getId(), cmb.getComment().getContent(),
					new UserCreatorDto(cmb.getComment().getUser().getId(),
							cmb.getComment().getUser().getName(),
							cmb.getComment().getUser().getAvatar(),
							cmb.getComment().getUser().getDesc(),
							cmb.getComment().getUser().getSlug()),
					cmb.getComment().getComment().getId(), cmb.getComment().getCreatedDate(),
					cmb.getComment().getUpdatedDate()));
		});

		return new ResponseEntity<>(ciDtos, HttpStatus.OK);
	}

	@GetMapping(path = "/blogs/{blogId}/comments/count/")
	@CrossOrigin
	public ResponseEntity<Object> countParentComment(@PathVariable("blogId") Integer blogId) {

		Blog blog = blogService.findById(blogId);
		if (blog == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		Integer countParentCmt = commentBlogService.countCommentBlogByBlog(blog);

		return new ResponseEntity<>(new TotalLikeDto(countParentCmt), HttpStatus.OK);
	}

	@GetMapping(path = "/blogs/{blogId}/comments/{parentCommentId}/count/")
	@CrossOrigin
	public ResponseEntity<Object> countChildComment(@PathVariable("blogId") Integer blogId,
			@PathVariable("parentCommentId") Integer parentCommentId) {

		Blog blog = blogService.findById(blogId);
		if (blog == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		Integer countChildCmt = commentBlogService.countChildCommentBlogByParentId(blog,
				parentCommentId);

		return new ResponseEntity<>(new TotalLikeDto(countChildCmt), HttpStatus.OK);
	}

	@GetMapping(path = "/comments/{commentId}/likes/checked/")
	@CrossOrigin
	public ResponseEntity<Object> checkedLikeComment(@PathVariable("commentId") Integer commentId) {
		User currentUser = userService.getCurrentLoginUser();
		Comment comment = commentService.findCommentById(commentId);

		if (comment == null || currentUser == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		LikeComment likeComment = likeCommentService.findLikeCommentByUserAndComment(currentUser,
				comment);

		Boolean hasLike = likeComment == null ? false : likeComment.getIsActived();

		return new ResponseEntity<>(new HasLikedDto(hasLike), HttpStatus.OK);
	}

	@GetMapping(path = "/comments/{commentId}/likes/count/")
	@CrossOrigin
	public ResponseEntity<Object> countLikeComment(@PathVariable("commentId") Integer commentId) {

		Comment comment = commentService.findCommentById(commentId);
		if (comment == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		Integer countLikeCmt = likeCommentService.countLikeCommentByComment(comment);

		return new ResponseEntity<>(new TotalLikeDto(countLikeCmt), HttpStatus.OK);
	}

	@PostMapping(path = "/comments/{commentId}/likes/")
	@CrossOrigin
	@Async
	public ResponseEntity<Object> toggleLikeComment(
			@PathVariable("commentId") Integer commentId) {

		Comment comment = commentService.findCommentById(commentId);
		User currentUser = userService.getCurrentLoginUser();

		if (comment == null || currentUser == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		LikeComment likeComment = likeCommentService
				.findLikeCommentByUserAndComment(currentUser, comment);

		if (likeComment != null) {
			likeComment.setIsActived(!likeComment.getIsActived());
			likeCommentService.saveLikeComment(likeComment);

			likeComment.setComment(comment);
			likeCommentService.saveLikeComment(likeComment);

			return new ResponseEntity<>(likeComment, HttpStatus.OK);
		}

		likeComment = new LikeComment();
		likeComment.setComment(comment);
		likeComment.setUser(currentUser);
		likeComment.setIsActived(true);
		likeCommentService.saveLikeComment(likeComment);

		return new ResponseEntity<>(likeComment, HttpStatus.CREATED);
	}

	@GetMapping(path = "/courses/")
	@CrossOrigin
	public ResponseEntity<Object> getAllCourses() {

		List<Course> courses = courseService.findAllCourses();

		List<Course> freeCourses = new ArrayList<>();
		List<Course> proCourses = new ArrayList<>();

		courses.forEach((c) -> {
			if (c.getIsFree())
				freeCourses.add(c);
			else
				proCourses.add(c);
		});

		return new ResponseEntity<>(new CoursesTypesDto(freeCourses, proCourses), HttpStatus.OK);
	}

	@GetMapping(path = "/courses/{courseId}/")
	@CrossOrigin
	public ResponseEntity<Object> getUserCourseByCourseId(
			@PathVariable("courseId") Integer courseId) {

		Course course = courseService.findCourseById(courseId);

		if (course == null)
			return new ResponseEntity<>(new MessageDto("Invalid course"), HttpStatus.NOT_FOUND);

		return new ResponseEntity<>(course, HttpStatus.OK);
	}

	@GetMapping("/courses/{courseId}/register/")
	@CrossOrigin
	public ResponseEntity<Object> addNewUserCourse(
			@PathVariable("courseId") Integer courseId) {

		User currentUser = userService.getCurrentLoginUser();
		Course course = courseService.findCourseById(courseId);

		UserCourse userCourses = userCourseService.findUserCourseByUserAndCourse(currentUser,
				course);
		if (currentUser == null || course == null)
			return new ResponseEntity<>(new MessageDto("Invalid user"), HttpStatus.NOT_FOUND);

		if (userCourses != null)
			return new ResponseEntity<>(new MessageDto("User has bought this course"),
					HttpStatus.UNAUTHORIZED);

		UserCourse uc = new UserCourse();
		uc.setUser(currentUser);
		uc.setCourse(course);
		uc.setJoinedDate(new Date());

		userCourseService.saveUserCourse(userCourses);

		return new ResponseEntity<>(uc, HttpStatus.OK);
	}

	@GetMapping("/courses/{courseId}/register/checked/")
	@CrossOrigin
	public ResponseEntity<Object> checkedExistUserCourse(
			@PathVariable("courseId") Integer courseId) {

		User currentUser = userService.getCurrentLoginUser();
		Course course = courseService.findCourseById(courseId);

		UserCourse userCourses = userCourseService.findUserCourseByUserAndCourse(currentUser,
				course);

		if (currentUser == null || course == null)
			return new ResponseEntity<>(new MessageDto("Invalid user"), HttpStatus.NOT_FOUND);

		Boolean isBought = userCourses != null ? true : false;

		return new ResponseEntity<>(isBought, HttpStatus.OK);
	}

	@GetMapping("/{slug}/registered-courses/show/")
	@CrossOrigin
	public ResponseEntity<Object> getAllUserCourseRegisteredBySlug(
			@PathVariable("slug") String slug) {

		User user = userService.findBySlug(slug);
		if (user == null)
			return new ResponseEntity<>(new MessageDto("Invalid user"), HttpStatus.NOT_FOUND);

		List<UserCourse> ucs = userCourseService.findUserCourseByUser(user);

		return new ResponseEntity<>(ucs, HttpStatus.OK);
	}

	@GetMapping("/registered-courses/")
	@CrossOrigin
	public ResponseEntity<Object> getAllUserCourseByCurrentUser() {
		User currentUser = userService.getCurrentLoginUser();

		if (currentUser == null)
			return new ResponseEntity<>(new MessageDto("Invalid user"), HttpStatus.NOT_FOUND);

		List<UserCourse> ucs = userCourseService.findUserCourseByUser(currentUser);

		List<Course> c = new ArrayList<>();

		ucs.forEach((uc) -> {
			c.add(uc.getCourse());
		});

		return new ResponseEntity<>(c, HttpStatus.OK);

	}

	@GetMapping("/courses/{courseId}/lessons/quick-view/")
	@CrossOrigin
	public ResponseEntity<Object> getQuickViewLessionByCourseId(
			@PathVariable("courseId") Integer courseId) {

		Course course = courseService.findCourseById(courseId);

		if (course == null)
			return new ResponseEntity<>(new MessageDto("Invalid course"), HttpStatus.NOT_FOUND);

		List<Lesson> lessons = lessonService.findByCourse(course);

		List<LessonQuickViewDto> lDto = new ArrayList<>();

		lessons.forEach((l) -> {
			lDto.add(new LessonQuickViewDto(l.getId(), l.getTitle()));
		});

		return new ResponseEntity<>(lDto, HttpStatus.OK);
	}

	@GetMapping(path = "/courses/{courseId}/lessons/")
	@CrossOrigin
	public ResponseEntity<Object> getAllLessonByCourse(
			@PathVariable("courseId") Integer courseId) {

		User currentUser = userService.getCurrentLoginUser();
		Course course = courseService.findCourseById(courseId);

		UserCourse userCourses = userCourseService.findUserCourseByUserAndCourse(currentUser,
				course);

		if (currentUser == null || course == null || userCourses == null)
			return new ResponseEntity<>(new MessageDto("Invalid user or course"),
					HttpStatus.NOT_FOUND);

		List<Lesson> lessons = course.getLessons();

		return new ResponseEntity<>(lessons, HttpStatus.OK);
	}


}
