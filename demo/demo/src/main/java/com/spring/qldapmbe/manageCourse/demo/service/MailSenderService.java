package com.spring.qldapmbe.manageCourse.demo.service;


import java.io.UnsupportedEncodingException;

import jakarta.mail.MessagingException;

public interface MailSenderService {
	void sendOtpEmail(String email)
			throws MessagingException, UnsupportedEncodingException;

}
