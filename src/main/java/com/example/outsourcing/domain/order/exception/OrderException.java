package com.example.outsourcing.domain.order.exception;

import com.example.outsourcing.common.exception.BaseCode;
import com.example.outsourcing.common.exception.CustomException;

public class OrderException extends CustomException {
	public OrderException(BaseCode baseCode) {
		super(baseCode);
	}
}
