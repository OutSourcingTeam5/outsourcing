package com.example.outsourcing.domain.user.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcing.common.response.CommonResponse;
import com.example.outsourcing.domain.user.dto.UserResponseDto;
import com.example.outsourcing.domain.user.dto.UserSignUpRequestDto;
import com.example.outsourcing.domain.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	/**
	 * 회원가입
	 */
	@PostMapping("/auth/signup")
	public CommonResponse<UserResponseDto> signUp(@RequestBody @Valid UserSignUpRequestDto requestDto) {
		UserResponseDto result = userService.signUp(requestDto);
		return CommonResponse.created(result);  // 201 Created
	}

	/**
	 * 회원 탈퇴
	 */
	@DeleteMapping("/users/withdraw")
	public CommonResponse<Void> withdraw(
		@RequestParam String email,
		@RequestParam String password
	) {
		userService.withdraw(email, password);
		return CommonResponse.ok();  // 200 OK
	}

	/**
	 * 마이페이지 (본인 정보 조회)
	 */
	@GetMapping("/users/me")
	public CommonResponse<UserResponseDto> getMyInfo(@RequestAttribute("userId") Long userId) {
		UserResponseDto result = userService.getMyInfo(userId);
		return CommonResponse.ok(result);  // 200 OK
	}

	/**
	 * 사용자 단건 조회 (관리자용)
	 */
	@GetMapping("/users/{id}")
	public CommonResponse<UserResponseDto> getUserById(@PathVariable Long id) {
		UserResponseDto result = userService.getUserById(id);
		return CommonResponse.ok(result);  // 200 OK
	}
}