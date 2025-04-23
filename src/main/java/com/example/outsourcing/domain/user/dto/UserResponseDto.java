package com.example.outsourcing.domain.user.dto;

import com.example.outsourcing.domain.user.entity.Role;

/**
 * 사용자 정보를 응답하기 위한 DTO입니다.
 */
public record UserResponseDto(
	Long id,
	String email,
	String nickname,
	Role role
) {
}