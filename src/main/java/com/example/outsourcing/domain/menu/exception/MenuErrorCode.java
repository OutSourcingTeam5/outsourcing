package com.example.outsourcing.domain.menu.exception;

import com.example.outsourcing.common.exception.BaseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MenuErrorCode implements BaseCode {
	MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "해당 메뉴가 존재하지 않습니다."),
	STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "해당 가게가 존재하지 않습니다."),
	FORBIDDEN(HttpStatus.FORBIDDEN, "C006", "권한이 없습니다."),
	ALREADY_DELETED_MENU(HttpStatus.CONFLICT, "M002", "이미 삭제된 메뉴입니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

	MenuErrorCode(HttpStatus httpStatus, String code, String message) {
		this.httpStatus = httpStatus;
		this.code = code;
		this.message = message;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
