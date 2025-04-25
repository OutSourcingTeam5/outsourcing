package com.example.outsourcing.domain.auth.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.outsourcing.domain.auth.dto.AuthResponseDto;
import com.example.outsourcing.domain.auth.dto.KakaoTokenResponse;
import com.example.outsourcing.domain.auth.dto.KakaoUserInfo;
import com.example.outsourcing.domain.auth.entity.RefreshToken;
import com.example.outsourcing.domain.auth.jwt.JwtProvider;
import com.example.outsourcing.domain.auth.repository.RefreshTokenRepository;
import com.example.outsourcing.domain.user.dto.UserResponseDto;
import com.example.outsourcing.domain.user.entity.Role;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {

	// application.properties 에 등록한 키와 정확히 일치시킵니다.
	@Value("${kakao.oauth.client-id}")
	private String clientId;

	@Value("${kakao.oauth.client-secret}")
	private String clientSecret;

	@Value("${kakao.oauth.redirect-uri}")
	private String redirectUri;

	private final WebClient webClient;
	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtProvider jwtProvider;

	@Transactional
	public AuthResponseDto loginWithKakao(String code) {
		// 1) 토큰 요청
		KakaoTokenResponse token = webClient.post()
			.uri("https://kauth.kakao.com/oauth/token")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.body(BodyInserters.fromFormData("grant_type", "authorization_code")
				.with("client_id", clientId)
				.with("client_secret", clientSecret)
				.with("redirect_uri", redirectUri)
				.with("code", code))
			.retrieve()
			.bodyToMono(KakaoTokenResponse.class)
			.block();

		// 2) 유저 정보 조회
		KakaoUserInfo info = webClient.get()
			.uri("https://kapi.kakao.com/v2/user/me")
			.header("Authorization", "Bearer " + token.getAccess_token())
			.retrieve()
			.bodyToMono(KakaoUserInfo.class)
			.block();

		String oauthId = String.valueOf(info.getId());

		// ─── 1) email final 로 선언 ──────────────────────────
		final String email;
		if (info.getKakao_account() != null && info.getKakao_account().getEmail() != null) {
			email = info.getKakao_account().getEmail();
		} else {
			email = "kakao_" + oauthId + "@oauth.local";
		}

		// ─── 2) 기존 유저 조회/신규 생성 ───────────────────────
		User user = userRepository.findByProviderId(oauthId)
			.orElseGet(() -> {
				User newUser = User.builder()
					.email(email)
					.password("")
					.name(info.getProperties().getNickname())
					.role(Role.USER)
					.provider("kakao")
					.providerId(oauthId)
					.build();
				return userRepository.save(newUser);
			});

		// ─── 3) JWT 발급 및 RefreshToken 저장 로직 ───────────────
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

		UserResponseDto dto = new UserResponseDto(
			user.getId(), user.getEmail(), user.getName(), user.getRole());
		return new AuthResponseDto(accessToken, dto);
	}
}