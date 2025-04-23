package com.example.outsourcing.domain.store.exception;

import com.example.outsourcing.common.exception.BaseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StoreErrorCode implements BaseCode {

    //임시 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001","요청하신 유저를 찾을 수 없습니다."),

    STORE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "S001", "사장 유저만 가게를 생성할 수 있습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
