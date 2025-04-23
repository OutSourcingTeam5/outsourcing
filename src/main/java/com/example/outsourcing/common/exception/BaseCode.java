package com.example.outsourcing.common.exception;

import org.springframework.http.HttpStatus;

public interface BaseCode {
	HttpStatus getHttpStatus();
	String getCode();
	String getMessage();
}