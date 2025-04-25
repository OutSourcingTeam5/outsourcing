package com.example.outsourcing.domain.review.exception;

import com.example.outsourcing.common.exception.BaseCode;
import com.example.outsourcing.common.exception.CustomException;

public class ReviewException extends CustomException {
	public ReviewException(BaseCode baseCode) {
		super(baseCode);
	}

	public ReviewException(BaseCode baseCode, String message) {
		super(baseCode, message);
	}
}
