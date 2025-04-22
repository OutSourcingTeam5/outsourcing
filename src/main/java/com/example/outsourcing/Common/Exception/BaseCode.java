package com.example.outsourcing.Common.Exception;

import org.springframework.http.HttpStatus;

public interface BaseCode {
	HttpStatus getStatus();
	String getMessage();
}