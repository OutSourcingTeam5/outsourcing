package com.example.outsourcing.domain.user;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.outsourcing.config.PasswordEncoder;
import com.example.outsourcing.domain.user.dto.UserResponseDto;
import com.example.outsourcing.domain.user.dto.UserSignUpRequestDto;
import com.example.outsourcing.domain.user.entity.Role;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.repository.UserRepository;
import com.example.outsourcing.domain.user.service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Test
	void 회원가입_성공() {
		// given
		String rawPassword = "Password1!";
		String encodedPassword = "encoded-password";
		String email = "test@example.com";
		String nickname = "테스트";
		Role role = Role.USER;

		UserSignUpRequestDto requestDto = new UserSignUpRequestDto(
			email, rawPassword, nickname, role
		);

		when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

		User savedUser = User.builder()
			.email(email)
			.password(encodedPassword)
			.name(nickname)
			.role(role)
			.provider(null)
			.providerId(null)
			.build();

		when(userRepository.save(any(User.class))).thenReturn(savedUser);

		// when
		UserResponseDto response = userService.signUp(requestDto);

		// then
		assertThat(response.email()).isEqualTo(email);
		assertThat(response.nickname()).isEqualTo(nickname);
		assertThat(response.role()).isEqualTo(role);

		verify(userRepository, times(1)).save(any(User.class));
		verify(passwordEncoder, times(1)).encode(rawPassword);
	}

	@Test
	void 회원가입_중복_이메일_예외() {
		// given
		String email = "test@example.com";
		String rawPassword = "Password1!";
		String nickname = "테스트";
		Role role = Role.USER;

		UserSignUpRequestDto requestDto = new UserSignUpRequestDto(email, rawPassword, nickname, role);

		// findByEmail(...)에 대한 mock 설정
		when(userRepository.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));

		// when & then
		assertThrows(IllegalArgumentException.class, () -> userService.signUp(requestDto));

		verify(userRepository, times(1)).findByEmail(email);
		verify(userRepository, never()).save(any());
		verify(passwordEncoder, never()).encode(any());
	}

	@Test
	void 회원탈퇴_성공() {
		// given
		String email = "test@example.com";
		String rawPassword = "Password1!";
		String encodedPassword = "encoded-password";

		User user = mock(User.class);

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
		when(user.isDeleted()).thenReturn(false);
		when(passwordEncoder.matches(rawPassword, user.getPassword())).thenReturn(true);

		// when
		userService.withdraw(email, rawPassword);

		// then
		verify(userRepository).findByEmail(email);
		verify(user).isDeleted();
		verify(passwordEncoder).matches(rawPassword, user.getPassword());
		verify(user).delete(); // soft delete 메서드 호출 여부
	}

	@Test
	void 회원탈퇴_실패_존재하지않는_이메일() {
		// given
		String email = "notfound@example.com";
		String password = "Password1!";

		when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

		// when & then
		assertThrows(NoSuchElementException.class, () ->
			userService.withdraw(email, password)
		);

		verify(userRepository).findByEmail(email);
		verifyNoMoreInteractions(userRepository, passwordEncoder);
	}

	@Test
	void 회원탈퇴_실패_이미_탈퇴한_사용자() {

	}
}