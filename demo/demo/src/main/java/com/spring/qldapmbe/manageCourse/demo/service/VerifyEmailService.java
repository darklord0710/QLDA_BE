package com.spring.qldapmbe.manageCourse.demo.service;

import com.spring.qldapmbe.manageCourse.demo.entity.VerifyEmail;

public interface VerifyEmailService {

	void saveVerifyEmail(VerifyEmail verifyEmail);

	VerifyEmail findByEmail(String email);

	Boolean isOtpExpiredTime(VerifyEmail verifyEmail);

	Boolean isOtpMatched(String otp, VerifyEmail verifyEmail);
}
