package com.example.outsourcing.domain.order.exception;

import org.springframework.http.HttpStatus;

import com.example.outsourcing.common.exception.BaseCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode implements BaseCode {
	UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "OR01", "로그인이 필요합니다."),
	STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "OR02", "존재하지 않는 가게입니다."),
	MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "OR03", "존재하지 않는 메뉴입니다."),
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "OR04", "존재하지 않는 주문입니다."),
	NOT_OWNER_OF_STORE(HttpStatus.FORBIDDEN, "OR05", "해당 가게의 사장님만 수정할 수 있습니다."),
	ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "OR06", "이미 완료된 주문입니다."),
	STORE_CLOSED(HttpStatus.BAD_REQUEST, "OR07", "가게가 현재 영업 중이 아닙니다."),
	ORDER_FROM_OWNER_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "OR08", "본인 가게에는 주문할 수 없습니다."),
	NO_DELETE_AUTHORITY(HttpStatus.BAD_REQUEST, "OR09", "주문자 또는 가게의 사장만 삭제할 수 있습니다."),
	CANNOT_DELETE_COMPLETED_ORDER(HttpStatus.BAD_REQUEST, "OR10", "완료된 주문은 삭제할 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
