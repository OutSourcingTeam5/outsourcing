package com.example.outsourcing.domain.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.outsourcing.domain.auth.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByUserId(Long userId);

	Optional<RefreshToken> findRefreshToken(String refreshToken);

	void deleteByUserId(Long userId);
}
