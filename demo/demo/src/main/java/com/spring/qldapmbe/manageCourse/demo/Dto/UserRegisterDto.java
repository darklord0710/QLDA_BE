package com.spring.qldapmbe.manageCourse.demo.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
	private String name;
	private String email;
	private String password;
	private String otp;
}
