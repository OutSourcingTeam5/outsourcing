package com.example.outsourcing.domain.test.exception;

import com.example.outsourcing.common.exception.BaseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TestErrorCode implements BaseCode {

    FAILED(HttpStatus.BAD_REQUEST, "T001", "에러메시지");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
