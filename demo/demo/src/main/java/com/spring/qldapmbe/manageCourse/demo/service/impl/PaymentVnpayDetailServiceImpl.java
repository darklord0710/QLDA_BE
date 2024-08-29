package com.spring.qldapmbe.manageCourse.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.qldapmbe.manageCourse.demo.entity.PaymentVnpayDetail;
import com.spring.qldapmbe.manageCourse.demo.repository.PaymentVnpayDetailRepository;
import com.spring.qldapmbe.manageCourse.demo.service.PaymentVnpayDetailService;

import jakarta.transaction.Transactional;

@Service
public class PaymentVnpayDetailServiceImpl implements PaymentVnpayDetailService {

	@Autowired
	private PaymentVnpayDetailRepository paymentVnpayDetailRepository;

	@Override
	@Transactional
	public void savePaymentBill(PaymentVnpayDetail paymentBill) {
		paymentVnpayDetailRepository.save(paymentBill);
	}

}
