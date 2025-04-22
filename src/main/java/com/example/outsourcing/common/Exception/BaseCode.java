package com.example.outsourcing.common.Exception;

import org.springframework.http.HttpStatus;

public interface BaseCode {
	HttpStatus getStatus();
	String getMessage();
}