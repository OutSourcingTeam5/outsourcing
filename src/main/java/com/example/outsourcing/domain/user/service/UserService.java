package com.example.outsourcing.domain.user.service;

import com.example.outsourcing.domain.user.dto.UserResponseDto;
import com.example.outsourcing.domain.user.dto.UserSignUpRequestDto;

/**
 * 사용자 관련 서비스 인터페이스입니다.
 */
public interface UserService {

	/**
	 * 회원가입을 처리합니다.
	 *
	 * @param request 회원가입 요청 DTO
	 * @return 가입된 사용자 정보
	 */
	UserResponseDto signUp(UserSignUpRequestDto request);

	/**
	 * 마이페이지 - 본인 정보 조회
	 *
	 * @param userId 현재 로그인한 사용자 ID
	 * @return 사용자 응답 DTO
	 */
	UserResponseDto getMyInfo(Long userId);

	/**
	 * 사용자 탈퇴 처리 (soft delete)
	 *
	 * @param email 이메일
	 * @param password 비밀번호
	 */
	void withdraw(String email, String password);

	/**
	 * 사용자 단건 조회 (관리자용 또는 상세페이지용)
	 *
	 * @param userId 사용자 ID
	 * @return 사용자 정보
	 */
	UserResponseDto getUserById(Long userId);
}
