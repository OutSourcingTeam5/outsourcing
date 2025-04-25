package com.example.outsourcing.domain.store.exception;

import com.example.outsourcing.common.exception.BaseCode;
import com.example.outsourcing.common.exception.CustomException;

public class StoreException extends CustomException {
	public StoreException(BaseCode baseCode) {
		super(baseCode);
	}

	public StoreException(BaseCode baseCode, String message) {
		super(baseCode, message);
	}
}
