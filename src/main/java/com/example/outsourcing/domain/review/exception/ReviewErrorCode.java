package com.example.outsourcing.domain.review.exception;

import org.springframework.http.HttpStatus;

import com.example.outsourcing.common.exception.BaseCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements BaseCode {

	//임시 에러
	REVIEW_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "요청하신 유저를 찾을 수 없습니다."),
	REVIEW_USER_NOT_LOGIN(HttpStatus.UNAUTHORIZED, "R002", "로그인 해주세요"),
	REVIEW_USER_NOT_CUSTOMER(HttpStatus.FORBIDDEN, "R003", "주문자만 리뷰를 생성할 수 있습니다."),
	REVIEW_ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "R005", "요청하신 주문을 찾을 수 없습니다."),
	REVIEW_NOT_ORDER_COMPLETE(HttpStatus.FORBIDDEN, "R006", "배달완료된 주문만 리뷰를 남길 수 있습니다"),
	REVIEW_RATING_WRONG(HttpStatus.BAD_REQUEST, "R007", "평점 을 다시 작성해주세요.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

}
