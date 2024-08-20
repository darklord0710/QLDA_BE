package com.spring.qldapmbe.manageCourse.demo.entity;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "paymentvnpaydetail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVnpayDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "amount")
	private float amount;

	@Column(name = "order_desc")
	private String orderDesc;

	@Column(name = "vnp_TransactionNo")
	private String vnpTransactionNo;

	@Column(name = "vnp_ResponseCode")
	private String vnpResponseCode;

	@OneToOne(fetch = FetchType.EAGER, cascade = {
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
	})
	@JoinColumn(name = "user_course_id", referencedColumnName = "id")
	private UserCourse userCourse;


}
