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

	STORE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "S001", "사장 유저만 가게를 생성할 수 있습니다."),
	STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "S002", "요청하신 가게를 찾을 수 없습니다."),
	STORE_LIMIT_REACHED(HttpStatus.BAD_REQUEST, "S003", "가게 생성은 최대 3개까지 가능합니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

}
