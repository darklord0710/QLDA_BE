package com.spring.qldapmbe.manageCourse.demo.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.qldapmbe.manageCourse.demo.config.PaymentVnpayConfig;
import com.spring.qldapmbe.manageCourse.demo.entity.Course;
import com.spring.qldapmbe.manageCourse.demo.entity.PaymentVnpayDetail;
import com.spring.qldapmbe.manageCourse.demo.entity.User;
import com.spring.qldapmbe.manageCourse.demo.entity.UserCourse;
import com.spring.qldapmbe.manageCourse.demo.service.CourseService;
import com.spring.qldapmbe.manageCourse.demo.service.PaymentVnpayDetailService;
import com.spring.qldapmbe.manageCourse.demo.service.UserCourseService;
import com.spring.qldapmbe.manageCourse.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/payment")
public class ApiPaymentRestController {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private UserService userService;
	private PaymentVnpayDetailService paymentVnpayDetailService;
	private Environment env;
	private CourseService courseService;
	private UserCourseService userCourseService;

	@Autowired
	public ApiPaymentRestController(HttpServletRequest req, HttpServletResponse resp,
			UserService userService, PaymentVnpayDetailService paymentVnpayDetailService,
			Environment env, CourseService courseService, UserCourseService userCourseService) {
		super();
		this.request = req;
		this.response = resp;
		this.userService = userService;
		this.paymentVnpayDetailService = paymentVnpayDetailService;
		this.env = env;
		this.courseService = courseService;
		this.userCourseService = userCourseService;
	}

	@PostMapping(path = "/course/{courseId}/")
	@CrossOrigin
	public ResponseEntity<Object> addCourseAndOrpayment(@PathVariable("courseId") Integer courseId)
			throws UnsupportedEncodingException {

		User currentUser = userService.getCurrentLoginUser();
		Course course = courseService.findCourseById(courseId);

		if (currentUser == null || course == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		UserCourse userCourse = userCourseService.findUserCourseByUserAndCourse(currentUser,
				course);

		if (userCourse != null)
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

		if (course.getPrice() == 0) {

			userCourse = new UserCourse();

			userCourse.setCourse(course);
			userCourse.setUser(currentUser);
			userCourse.setJoinedDate(new Date());
			userCourseService.saveUserCourse(userCourse);

			return new ResponseEntity<>(userCourse, HttpStatus.OK);
		}

		String vnp_Version = "2.1.0"; // phiên bản
		String vnp_Command = "pay";
		String orderType = "billpayment"; // loại thanh toán
		long amount = course.getPrice() * 100;
		String bankCode = "";

		String vnp_TxnRef = PaymentVnpayConfig.getRandomNumber(8); // tạo id random
		String vnp_IpAddr = PaymentVnpayConfig.getIpAddress(request);

		String vnp_TmnCode = PaymentVnpayConfig.vnp_TmnCode; // mã bí mật trong file .properties

		Map<String, String> vnp_Params = new HashMap<>(); // tạo chuỗi param để gửi cùng url vnp
		vnp_Params.put("vnp_Version", vnp_Version);
		vnp_Params.put("vnp_Command", vnp_Command);
		vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
		vnp_Params.put("vnp_Amount", String.valueOf(amount));
		vnp_Params.put("vnp_CurrCode", "VND");

		if (bankCode != null && !bankCode.isEmpty()) {
			vnp_Params.put("vnp_BankCode", bankCode); // nếu bankcode rỗng thì chọn ngân hàng
		}
		vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
		vnp_Params.put("vnp_OrderInfo",
				currentUser.getEmail() + "_" + course.getId());
		vnp_Params.put("vnp_OrderType", orderType);

		String locate = request.getParameter("language");
		if (locate != null && !locate.isEmpty()) {
			vnp_Params.put("vnp_Locale", locate);
		} else {
			vnp_Params.put("vnp_Locale", "vn");
		}
		vnp_Params.put("vnp_ReturnUrl", PaymentVnpayConfig.vnp_ReturnUrl); // url return sau khi
		vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

		Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String vnp_CreateDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

		cld.add(Calendar.MINUTE, 15);
		String vnp_ExpireDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

		List fieldNames = new ArrayList(vnp_Params.keySet());
		Collections.sort(fieldNames);
		StringBuilder hashData = new StringBuilder();
		StringBuilder query = new StringBuilder();
		Iterator itr = fieldNames.iterator();
		while (itr.hasNext()) {
			String fieldName = (String) itr.next();
			String fieldValue = (String) vnp_Params.get(fieldName);
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				// Build hash data
				hashData.append(fieldName);
				hashData.append('=');
				hashData.append(
						URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				// Build query
				query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
				query.append('=');
				query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				if (itr.hasNext()) {
					query.append('&');
					hashData.append('&');
				}
			}
		}
		String queryUrl = query.toString();
		String vnp_SecureHash = PaymentVnpayConfig.hmacSHA512(PaymentVnpayConfig.secretKey,
				hashData.toString());
		queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

		String paymentUrl = PaymentVnpayConfig.vnp_PayUrl + "?" + queryUrl; // trả về url cuối cùng
																			// kèm params

		return new ResponseEntity<>(paymentUrl, HttpStatus.OK);
	}

	@GetMapping(path = "/payment_return/") // xử lý dữ liệu trả về
	@CrossOrigin
	public ResponseEntity<PaymentVnpayDetail> payment_return(
			@RequestParam Map<String, String> params) throws IOException {
		String vnpResponseCode = params.get("vnp_ResponseCode");
		Long amount = Long.parseLong(params.get("vnp_Amount")) / 100;

		if (vnpResponseCode.equals("00")) {

			String orderInfo = params.get("vnp_OrderInfo");
			String parts[] = orderInfo.split("_");
			User user = userService.findByEmail(parts[0]);
			Course course = courseService.findCourseById(Integer.parseInt(parts[1]));
			UserCourse userCourse = new UserCourse();
			userCourse.setCourse(course);
			userCourse.setUser(user);
			userCourse.setJoinedDate(new Date());
			userCourseService.saveUserCourse(userCourse);

			PaymentVnpayDetail paymentVnpayDetail = new PaymentVnpayDetail();
			paymentVnpayDetail.setUserCourse(userCourse);
			paymentVnpayDetail.setAmount(amount);
			paymentVnpayDetail.setOrderDesc(String.format(
					"Thanh toán khóa học",
					course.getTitle(), user.getEmail()));
			paymentVnpayDetail.setVnpTransactionNo(params.get("vnp_TransactionNo"));
			paymentVnpayDetail.setVnpResponseCode(params.get("vnp_ResponseCode"));

			paymentVnpayDetailService.savePaymentBill(paymentVnpayDetail);

			return new ResponseEntity<PaymentVnpayDetail>(paymentVnpayDetail, HttpStatus.OK);
		} else {
			return new ResponseEntity<PaymentVnpayDetail>(HttpStatus.NOT_ACCEPTABLE);
		}
	}

}
