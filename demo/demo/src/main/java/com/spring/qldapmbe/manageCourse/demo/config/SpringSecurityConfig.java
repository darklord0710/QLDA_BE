package com.spring.qldapmbe.manageCourse.demo.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@Order(2)
public class SpringSecurityConfig {

	@Bean
	public SecurityFilterChain springFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/login/**", "/error", "/public/resources/**")
				.permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers("/chatting").hasAnyRole("ADMIN", "USER")
				.anyRequest().authenticated())
				.formLogin((form) -> form
						.usernameParameter("email").passwordParameter("password")
						.defaultSuccessUrl("/admin").permitAll())
				.logout((logout) -> logout.permitAll())
				.cors(cors -> cors.disable())
				.csrf(csrf -> csrf.disable());

		return http.build();
	}

	@Bean
	public static AuthenticationSuccessHandler successHandler() {
		return new SimpleUrlAuthenticationSuccessHandler() {

			@Override
			protected String determineTargetUrl(HttpServletRequest request,
					HttpServletResponse response,
					Authentication authentication) {
				// Lấy danh sách các vai trò của người dùng

				Collection<? extends GrantedAuthority> authorities = authentication
						.getAuthorities();

				List<String> roles = new ArrayList<>();
				for (GrantedAuthority authority : authorities) {
					roles.add(authority.getAuthority());
				}

				// Kiểm tra nếu người dùng có vai trò ADMIN
				if (roles.contains("ROLE_ADMIN")) {
					return "/admin"; // Chuyển hướng đến trang dashboard của admin
				} else {
					return "/chatting"; // Chuyển hướng đến trang chatting của user
				}
			}
		};
	}

}
