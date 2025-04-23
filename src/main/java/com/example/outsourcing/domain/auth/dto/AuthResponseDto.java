package com.example.outsourcing.domain.auth.dto;

import com.example.outsourcing.domain.user.dto.UserResponseDto;

/**
 * 로그인 후 응답 DTO: 토큰 + 사용자 정보
 */
public record AuthResponseDto(
	String accessToken,
	UserResponseDto user
) {
}

