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
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.spring.qldapmbe.manageCourse.demo.Dto.CreateCommentBlogOutputDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.EmailDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.HasLikedDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.TotalLikeDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.UpdateContentCmtDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.UpdatedNameDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.UserCreatorDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.UserLoginDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.UserRegisterDto;
import com.spring.qldapmbe.manageCourse.demo.config.JwtService;
import com.spring.qldapmbe.manageCourse.demo.entity.Blog;
import com.spring.qldapmbe.manageCourse.demo.entity.Comment;
import com.spring.qldapmbe.manageCourse.demo.entity.CommentBlog;
import com.spring.qldapmbe.manageCourse.demo.entity.LikeBlog;
import com.spring.qldapmbe.manageCourse.demo.entity.LikeComment;
import com.spring.qldapmbe.manageCourse.demo.entity.User;
import com.spring.qldapmbe.manageCourse.demo.entity.VerifyEmail;
import com.spring.qldapmbe.manageCourse.demo.service.BlogService;
import com.spring.qldapmbe.manageCourse.demo.service.CommentBlogService;
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
	private CourseService courseService;
	private UserCourseService userCourseService;

	@Autowired
	public ApiUserRestController(JwtService jwtService, UserService userService,
			MailSenderService mailSenderService, VerifyEmailService verifyEmailService,
			BlogService blogService, LikeBlogService likeBlogService,
			CommentService commentService, CommentBlogService commentBlogService,
			LikeCommentService likeCommentService, LessonService lessonService,
			CourseService courseService,
			UserCourseService userCourseService) {
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
		this.courseService = courseService;
		this.userCourseService = userCourseService;
	}

	@PostMapping(path = "/auth/login/")
	@CrossOrigin
	public ResponseEntity<String> login(@RequestBody UserLoginDto loginDto) {

		if (!userService.authUser(loginDto.getEmail(), loginDto.getPassword()))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		String token = jwtService.generateTokenLogin(loginDto.getEmail(),
				loginDto.getPassword());

		return new ResponseEntity<>(token, HttpStatus.OK);

	}

	@GetMapping(path = "/current-user/", produces = {
			MediaType.APPLICATION_JSON_VALUE
	})
	@CrossOrigin
	public ResponseEntity<User> getCurrentUserApi() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			User user = userService.findByEmail((authentication.getName()));
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
		return null;
	}

	@PostMapping(path = "/auth/check-email/")
	@CrossOrigin
	public ResponseEntity<String> checkExistEmail(@RequestBody EmailDto emailDto)
			throws UnsupportedEncodingException, MessagingException {

		User existUser = userService.findByEmail(emailDto.getEmail());
		if (existUser != null)
			return new ResponseEntity<>("This email is existed !", HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>("Successfully !", HttpStatus.OK);
	}

	@PostMapping(path = "/auth/verify-email/")
	@CrossOrigin
	public ResponseEntity<String> retrieveOtp(@RequestBody EmailDto emailDto)
			throws UnsupportedEncodingException, MessagingException {

		User existUser = userService.findByEmail(emailDto.getEmail());
		if (existUser != null)
			return new ResponseEntity<>("This email is existed !", HttpStatus.BAD_REQUEST);

		mailSenderService.sendOtpEmail(emailDto.getEmail());

		return new ResponseEntity<>("Sent mail successfully !", HttpStatus.OK);
	}

	@PostMapping(path = "/auth/register/")
	@CrossOrigin
	public ResponseEntity<User> register(@RequestBody UserRegisterDto registerDto) {

		User existUser = userService.findByEmail(registerDto.getEmail());
		VerifyEmail verifyEmail = verifyEmailService.findByEmail(registerDto.getEmail());

		if (existUser != null || verifyEmail == null)
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (verifyEmailService.isOtpExpiredTime(verifyEmail))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		if (!verifyEmailService.isOtpMatched(registerDto.getOtp(), verifyEmail))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		userService.saveUserRegisterDto(registerDto);
		existUser = userService.findByEmail(registerDto.getEmail());

		return new ResponseEntity<>(existUser, HttpStatus.CREATED);

	}

	@PatchMapping(path = "/updated-name/")
	@CrossOrigin
	public ResponseEntity<User> updatedName(@RequestBody UpdatedNameDto nameDto) {
		User user = userService.getCurrentLoginUser();
		user.setName(nameDto.getName());
		userService.saveUser(user);

		return new ResponseEntity<>(user, HttpStatus.OK);

	}

	@PatchMapping(path = "/updated-avatar/", consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@CrossOrigin
	public ResponseEntity<User> updatedAvatar(@RequestPart("avatar") MultipartFile file) {
		User user = userService.getCurrentLoginUser();

		if (file == null || file.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (user != null) {
			user.setFile(file);
			userService.setCloudinaryField(user);

			return new ResponseEntity<>(user, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

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

	@PostMapping(path = "/blog/")
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

	@GetMapping(path = "/blog/{blogId}/")
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

}
