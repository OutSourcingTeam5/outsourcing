package com.example.outsourcing.domain.auth.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtProvider {

	@Value("${jwt.secret-key}")
	private String secretKey;

	private Key key;

	private final long ACCESS_EXPIRATION = 1000 * 60 * 60; // 1시간

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	/**
	 * AccessToken 생성
	 */
	public String createAccessToken(Long userId) {
		Date now = new Date();
		return Jwts.builder()
			.setSubject(userId.toString())
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + ACCESS_EXPIRATION))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	/**
	 * JWT에서 userId 추출
	 */
	public Long getUserId(String token) {
		Claims claims = parseClaims(token);
		return Long.parseLong(claims.getSubject());
	}

	/**
	 * 토큰 유효성 검사
	 */
	public boolean validateToken(String token) {
		try {
			parseClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	private Claims parseClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public String createRefreshToken(Long userId) {
		Date now = new Date();
		return Jwts.builder()
			.setSubject(userId.toString())
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + (1000L * 60 * 60 * 24 * 7))) // 7일
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}
}
