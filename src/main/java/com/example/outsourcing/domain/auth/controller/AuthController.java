package com.example.outsourcing.domain.auth.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.outsourcing.common.response.CommonResponse;
import com.example.outsourcing.config.PasswordEncoder;
import com.example.outsourcing.domain.auth.dto.AuthResponseDto;
import com.example.outsourcing.domain.auth.entity.RefreshToken;
import com.example.outsourcing.domain.auth.jwt.JwtProvider;
import com.example.outsourcing.domain.auth.repository.RefreshTokenRepository;
import com.example.outsourcing.domain.user.dto.UserLoginRequestDto;
import com.example.outsourcing.domain.user.dto.UserResponseDto;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	/**
	 * 일반 로그인 (AccessToken + RefreshToken 발급)
	 */
	@PostMapping("/login")
	public CommonResponse<AuthResponseDto> login(@RequestBody UserLoginRequestDto requestDto) {
		User user = userRepository.findByEmail(requestDto.email())
			.orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다."));

		if (user.isDeleted()) {
			throw new IllegalStateException("탈퇴한 사용자입니다.");
		}

		if (!passwordEncoder.matches(requestDto.password(), user.getPassword())) {
			throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
		}

		//  Access & Refresh Token 발급
		String accessToken = jwtProvider.createAccessToken(user.getId());
		String refreshToken = jwtProvider.createRefreshToken(user.getId());

		//  DB에 RefreshToken 저장 (기존 값이 있다면 갱신)
		refreshTokenRepository.findByUserId(user.getId()).ifPresentOrElse(
			existingToken -> existingToken.updateToken(refreshToken, LocalDateTime.now().plusDays(7)),
			() -> refreshTokenRepository.save(
				RefreshToken.builder()
					.user(user)
					.refreshToken(refreshToken)
					.expiredAt(LocalDateTime.now().plusDays(7))
					.build()
			)
		);

		UserResponseDto userDto = new UserResponseDto(user.getId(), user.getEmail(), user.getName(), user.getRole());
		AuthResponseDto responseDto = new AuthResponseDto(accessToken, userDto);

		return CommonResponse.ok(responseDto);
	}

	/**
	 * AccessToken 재발급 (RefreshToken 검증)
	 */
	@PostMapping("/token/refresh")
	public CommonResponse<String> refreshToken(@RequestHeader("Authorization") String refreshToken) {
		RefreshToken saved = refreshTokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));

		if (saved.getExpiredAt().isBefore(LocalDateTime.now())) {
			throw new IllegalStateException("만료된 리프레시 토큰입니다.");
		}

		String newAccessToken = jwtProvider.createAccessToken(saved.getUser().getId());
		return CommonResponse.ok(newAccessToken);
	}

	/**
	 * 로그아웃 (RefreshToken 삭제)
	 */
	@PostMapping("/logout")
	public CommonResponse<Void> logout(@RequestAttribute("userId") Long userId) {
		refreshTokenRepository.deleteByUserId(userId);
		return CommonResponse.ok();
	}
}