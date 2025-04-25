package com.example.outsourcing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient 를 빈으로 등록하는 설정 클래스
 */
@Configuration
public class WebClientConfig {

	@Bean
	public WebClient webClient() {
		return WebClient.builder().build();
	}
}
