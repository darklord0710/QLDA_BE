package com.spring.qldapmbe.manageCourse.demo.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeDto {
	private String currentPassword;
	private String newPassword;
}
