package com.spring.qldapmbe.manageCourse.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.qldapmbe.manageCourse.demo.entity.VerifyEmail;



@Repository
public interface VerifyEmailRepository extends JpaRepository<VerifyEmail, Integer> {

	VerifyEmail findByEmail(String email);
}
