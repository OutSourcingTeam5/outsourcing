package com.example.outsourcing.domain.user.dto;

import com.example.outsourcing.domain.user.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 회원가입 요청을 위한 DTO
 */
public record UserSignUpRequestDto(

	@Email(message = "이메일 형식이 아닙니다.")
	@NotBlank(message = "이메일은 필수입니다.")
	String email,

	@NotBlank(message = "비밀번호는 필수입니다.")
	@Pattern(
		regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
		message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 포함한 8자 이상이어야 합니다."
	)
	String password,

	@NotBlank(message = "닉네임은 필수입니다.")
	@Size(max = 50)
	String nickname,

	Role role
) {
}
