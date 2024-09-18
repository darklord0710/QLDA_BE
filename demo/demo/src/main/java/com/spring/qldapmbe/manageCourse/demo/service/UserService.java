package com.spring.qldapmbe.manageCourse.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.spring.qldapmbe.manageCourse.demo.Dto.UserRegisterDto;
import com.spring.qldapmbe.manageCourse.demo.entity.User;


public interface UserService extends UserDetailsService {

	User findByEmail(String email);

	void saveUser(User user);

	void saveUserRegisterDto(UserRegisterDto registerDto);

	boolean authUser(String email, String password);

	User getCurrentLoginUser();

	void setCloudinaryField(User user);

	User findBySlug(String slug);

}
