package com.example.outsourcing.domain.store.exception;

import org.springframework.http.HttpStatus;

import com.example.outsourcing.common.exception.BaseCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreErrorCode implements BaseCode {

	//임시 에러
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "요청하신 유저를 찾을 수 없습니다."),

	STORE_FORBIDDEN_NORMAL(HttpStatus.FORBIDDEN, "S001", "일반 유저는 권한이 없습니다."),
	STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "S002", "요청하신 가게를 찾을 수 없습니다."),
	STORE_LIMIT_REACHED(HttpStatus.BAD_REQUEST, "S003", "가게 생성은 최대 3개까지 가능합니다."),
	STORE_FORBIDDEN(HttpStatus.FORBIDDEN, "S004", "가게 사장만 가게 정보를 수정/삭제 할 수 있습니다."),
	STORE_USER_NOT_LOGIN(HttpStatus.UNAUTHORIZED, "S005", "로그인 해주세요");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

}
