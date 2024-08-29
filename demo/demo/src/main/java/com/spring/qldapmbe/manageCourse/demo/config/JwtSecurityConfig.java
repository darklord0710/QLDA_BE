package com.spring.qldapmbe.manageCourse.demo.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.spring.qldapmbe.manageCourse.demo.filter.CustomAccessDeniedHandler;
import com.spring.qldapmbe.manageCourse.demo.filter.JwtAuthenticationTokenFilter;
import com.spring.qldapmbe.manageCourse.demo.filter.RestAuthenticationEntryPoint;



@Configuration
@EnableWebSecurity
@EnableTransactionManagement
@Order(1)
public class JwtSecurityConfig {


	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() throws Exception {
		JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter = new JwtAuthenticationTokenFilter();
		return jwtAuthenticationTokenFilter;
	}

	@Bean
	public RestAuthenticationEntryPoint restServicesEntryPoint() {
		return new RestAuthenticationEntryPoint();
	}

	@Bean
	public CustomAccessDeniedHandler customAccessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	@Bean
	public SecurityFilterChain jwtSecurityfilterChain(HttpSecurity http) throws Exception {

		http.securityMatcher("/api/**").authorizeHttpRequests(auth -> auth

				.requestMatchers(HttpMethod.GET,
						"/api/users/blog/{blogId}/comments/",
						"/api/users/comments/{parentCommentId}/",
						"/api/users/lesson/{lessonId}/comments/",
						"/api/payment/payment_return/")
				.permitAll()

				.requestMatchers(HttpMethod.POST,
						"/api/users/login/",
						"/api/users/register/",
						"/api/users/verify-email/")
				.permitAll()

				.requestMatchers(HttpMethod.GET,
						"/api/users/current-user/",
						"/api/users/blog/{blogId}/",
						"/api/users/blogs/",
						"/api/users/courses/",
						"/api/users/courses/{courseId}/lessons/")
				.hasRole("USER")

				.requestMatchers(HttpMethod.POST,
						"/api/users/updated-name/",
						"/api/users/updated-avatar/",
						"/api/users/blog/",
						"/api/users/toggle-like-blog/{blogId}/",
						"/api/users/comment-blog/{blogId}/",
						"/api/users/toggle-like-comment/{commentId}/",
						"/api/users/comment-lesson/{lessonId}/",
						"/api/users/add-course/{courseId}/",
						"/api/payment/course/{courseId}/")
				.hasRole("USER")

				.anyRequest().authenticated())
				.httpBasic(httpbc -> httpbc
						.authenticationEntryPoint(restServicesEntryPoint()))
				.sessionManagement(smc -> smc
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(exp -> exp.authenticationEntryPoint(restServicesEntryPoint())
						.accessDeniedHandler(customAccessDeniedHandler()))
				.addFilterBefore(
						jwtAuthenticationTokenFilter(),
						UsernamePasswordAuthenticationFilter.class)
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(csrf -> csrf.disable());
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.addAllowedHeader("*");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	


}
