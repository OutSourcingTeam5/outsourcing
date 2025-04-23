package com.example.outsourcing.domain.test.exception;

import com.example.outsourcing.common.exception.BaseCode;
import com.example.outsourcing.common.exception.CustomException;

public class TestException extends CustomException {
    public TestException(BaseCode baseCode) {
        super(baseCode);
    }

    public TestException(BaseCode baseCode, String message) {
        super(baseCode, message);
    }
}
