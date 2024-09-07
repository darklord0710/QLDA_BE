package com.spring.qldapmbe.manageCourse.demo.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInitDto {

	private Integer amount;
	private Integer courseId;

}
