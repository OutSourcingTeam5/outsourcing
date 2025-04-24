package com.example.outsourcing.common.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode implements BaseCode {

	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "유효하지 않은 입력 값입니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "허용되지 않은 요청 방식입니다."),
	ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "C003", "요청한 엔티티를 찾을 수 없습니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C004", "내부 서버 오류가 발생했습니다."),
	INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C005", "유효하지 않은 타입의 값입니다."),

	// 메뉴 도메인 에러코드 (추가)
	MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "해당 메뉴가 존재하지 않습니다."),
	STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "해당 가게가 존재하지 않습니다."),
	FORBIDDEN(HttpStatus.FORBIDDEN, "C006", "권한이 없습니다."),
	ALREADY_DELETED_MENU(HttpStatus.CONFLICT, "M002", "이미 삭제된 메뉴입니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
