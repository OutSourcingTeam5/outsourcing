package com.example.outsourcing.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 로그인 요청을 위한 DTO
 */
public record UserLoginRequestDto(

	@Email(message = "이메일 형식이 아닙니다.")
	@NotBlank(message = "이메일은 필수입니다.")
	String email,

	@NotBlank(message = "비밀번호는 필수입니다.")
	String password
) {
}
