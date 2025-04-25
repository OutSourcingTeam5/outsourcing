package com.example.outsourcing.domain.auth.dto;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserInfo {
	private Long id;
	private KakaoProperties properties;
	private KakaoAccount kakao_account;

	@Getter
	@Setter
	public static class KakaoProperties {
		private String nickname;
		private String profile_image;
	}

	@Getter
	@Setter
	public static class KakaoAccount {
		private Boolean profile_needs_agreement;
		private Map<String, Object> profile;
		private Boolean has_email;
		private String email;
	}
}
