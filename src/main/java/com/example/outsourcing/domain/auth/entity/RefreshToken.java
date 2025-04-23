package com.example.outsourcing.domain.auth.entity;

import java.time.LocalDateTime;

import com.example.outsourcing.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자별 리프레시 토큰을 저장하는 엔티티입니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "refresh_tokens")
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 리프레시 토큰을 소유한 사용자
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

	/**
	 * JWT 리프레시 토큰 문자열
	 */
	@Column(nullable = false, length = 512)
	private String refreshToken;

	/**
	 * 만료 시각
	 */
	@Column(nullable = false)
	private LocalDateTime expiredAt;

	/**
	 * 토큰 갱신 (재발급 시 사용)
	 */
	public void updateToken(String refreshToken, LocalDateTime expiredAt) {
		this.refreshToken = refreshToken;
		this.expiredAt = expiredAt;
	}
}