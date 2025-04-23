package com.example.outsourcing.common.exception;

import org.springframework.http.HttpStatus;

public interface BaseCode {
	HttpStatus getStatus();

	String getMessage();
}