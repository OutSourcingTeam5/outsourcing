package com.example.outsourcing.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.outsourcing.domain.auth.jwt.JwtAuthenticationFilter;
import com.example.outsourcing.domain.auth.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final JwtProvider jwtProvider;

	@Bean
	public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter() {
		FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new JwtAuthenticationFilter(jwtProvider));
		registration.addUrlPatterns("/api/*");
		registration.setOrder(1);
		return registration;
	}
}