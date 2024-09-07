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

import com.spring.qldapmbe.manageCourse.demo.Dto.BlogDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.BlogDtoOutputById;
import com.spring.qldapmbe.manageCourse.demo.Dto.BlogItemDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.EmailDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.UpdatedNameDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.UserCreatorDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.UserLoginDto;
import com.spring.qldapmbe.manageCourse.demo.Dto.UserRegisterDto;
import com.spring.qldapmbe.manageCourse.demo.config.JwtService;
import com.spring.qldapmbe.manageCourse.demo.entity.Blog;
import com.spring.qldapmbe.manageCourse.demo.entity.User;
import com.spring.qldapmbe.manageCourse.demo.entity.VerifyEmail;
import com.spring.qldapmbe.manageCourse.demo.service.BlogService;
import com.spring.qldapmbe.manageCourse.demo.service.MailSenderService;
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


	@Autowired
	public ApiUserRestController(JwtService jwtService, UserService userService,
			MailSenderService mailSenderService, VerifyEmailService verifyEmailService,
			BlogService blogService) {
		super();
		this.jwtService = jwtService;
		this.userService = userService;
		this.mailSenderService = mailSenderService;
		this.verifyEmailService = verifyEmailService;
		this.blogService = blogService;

	}

	@PostMapping(path = "/login/")
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

	@PostMapping(path = "/verify-email/")
	@CrossOrigin
	public ResponseEntity<String> retrieveOtp(@RequestBody EmailDto emailDto)
			throws UnsupportedEncodingException, MessagingException {

		User existUser = userService.findByEmail(emailDto.getEmail());
		if (existUser != null)
			return new ResponseEntity<>("This email is existed !", HttpStatus.UNAUTHORIZED);

		mailSenderService.sendOtpEmail(emailDto.getEmail());

		return new ResponseEntity<>("Sent mail successfully !", HttpStatus.OK);
	}

	@PostMapping(path = "/register/")
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

}
