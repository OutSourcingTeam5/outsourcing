package com.example.outsourcing.domain.auth.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.outsourcing.common.response.CommonResponse;
import com.example.outsourcing.config.PasswordEncoder;
import com.example.outsourcing.domain.auth.dto.AuthResponseDto;
import com.example.outsourcing.domain.auth.entity.RefreshToken;
import com.example.outsourcing.domain.auth.jwt.JwtProvider;
import com.example.outsourcing.domain.auth.repository.RefreshTokenRepository;
import com.example.outsourcing.domain.auth.service.KakaoOAuthService;
import com.example.outsourcing.domain.user.dto.UserLoginRequestDto;
import com.example.outsourcing.domain.user.dto.UserResponseDto;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final KakaoOAuthService kakaoOAuthService;

	// application.properties 에 정의한 값과 정확히 일치해야 합니다.
	@Value("${kakao.oauth.client-id}")
	private String kakaoClientId;

	@Value("${kakao.oauth.redirect-uri}")
	private String kakaoRedirectUri;

	/**
	 * 일반 로그인 (Email/Pw)
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

		String accessToken = jwtProvider.createAccessToken(user.getId());
		String refreshToken = jwtProvider.createRefreshToken(user.getId());

		refreshTokenRepository.findByUserId(user.getId())
			.ifPresentOrElse(
				existing -> existing.updateToken(refreshToken, LocalDateTime.now().plusDays(7)),
				() -> refreshTokenRepository.save(
					RefreshToken.builder()
						.user(user)
						.refreshToken(refreshToken)
						.expiredAt(LocalDateTime.now().plusDays(7))
						.build()
				)
			);

		AuthResponseDto dto = new AuthResponseDto(
			accessToken,
			new UserResponseDto(user.getId(), user.getEmail(), user.getName(), user.getRole())
		);
		return CommonResponse.ok(dto);
	}

	/**
	 * RefreshToken 으로 AccessToken 재발급
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
	@Transactional
	@PostMapping("/logout")
	public CommonResponse<Void> logout(@RequestAttribute("userId") Long userId) {
		refreshTokenRepository.deleteByUserId(userId);
		return CommonResponse.ok();
	}

	/**
	 * ① 카카오 인가 페이지로 리다이렉트
	 */
	@GetMapping("/login/kakao")
	public void redirectToKakao(HttpServletResponse response) throws IOException, IOException {
		String uri = UriComponentsBuilder
			.fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
			.queryParam("client_id", kakaoClientId)
			.queryParam("redirect_uri", kakaoRedirectUri)
			.queryParam("response_type", "code")
			.build()
			.toUriString();
		response.sendRedirect(uri);
	}

	/**
	 * ② 카카오 콜백 처리
	 */
	@GetMapping("/login/kakao/callback")
	public CommonResponse<AuthResponseDto> kakaoCallback(@RequestParam("code") String code) {
		AuthResponseDto authDto = kakaoOAuthService.loginWithKakao(code);
		return CommonResponse.ok(authDto);
	}

}