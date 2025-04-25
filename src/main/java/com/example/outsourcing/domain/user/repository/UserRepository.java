package com.example.outsourcing.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.outsourcing.domain.user.entity.User;

/**
 * 사용자 정보를 조회/저장하기 위한 JPA Repository 인터페이스입니다.
 */
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * 이메일로 사용자 조회
	 *
	 * @param email 이메일
	 * @return Optional<User>
	 */
	Optional<User> findByEmail(String email);

	/**
	 * OAuth providerId로 사용자 조회
	 *
	 * @param providerId 외부 OAuth 제공자의 사용자 ID
	 * @return Optional<User>
	 */
	Optional<User> findByProviderId(String providerId);

	/**
	 * 탈퇴 여부 포함하여 이메일로 조회
	 *
	 * @param email 이메일
	 * @param isDeleted 삭제 여부
	 * @return Optional<User>
	 */
	Optional<User> findByEmailAndIsDeleted(String email, boolean isDeleted);

	Object existsByEmail(String email);

	Optional<User> findByProviderAndProviderId(String provider, String providerId);
}
