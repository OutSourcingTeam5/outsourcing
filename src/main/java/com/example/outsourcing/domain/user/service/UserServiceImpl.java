package com.example.outsourcing.domain.user.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.outsourcing.config.PasswordEncoder;
import com.example.outsourcing.domain.user.dto.UserResponseDto;
import com.example.outsourcing.domain.user.dto.UserSignUpRequestDto;
import com.example.outsourcing.domain.user.entity.Role;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * 사용자 관련 서비스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public UserResponseDto signUp(UserSignUpRequestDto request) {
		if (userRepository.findByEmail(request.email()).isPresent()) {
			throw new IllegalArgumentException("이미 가입된 이메일입니다.");
		}

		String encodedPassword = passwordEncoder.encode(request.password());

		User user = User.builder()
			.email(request.email())
			.password(encodedPassword)
			.name(request.nickname())
			.role(request.role() != null ? request.role() : Role.USER)
			.build();

		User savedUser = userRepository.save(user);
		return toDto(savedUser);
	}

	@Override
	@Transactional
	public void withdraw(String email, String password) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

		if (user.isDeleted()) {
			throw new IllegalStateException("이미 탈퇴한 사용자입니다.");
		}

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		user.delete(); // soft delete
	}

	@Override
	@Transactional
	public UserResponseDto getMyInfo(Long userId) {
		User user = userRepository.findById(userId)
			.filter(u -> !u.isDeleted())
			.orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

		return toDto(user);
	}

	@Override
	@Transactional
	public UserResponseDto getUserById(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

		return toDto(user);
	}

	private UserResponseDto toDto(User user) {
		return new UserResponseDto(
			user.getId(),
			user.getEmail(),
			user.getName(),
			user.getRole()
		);
	}
}