package com.example.outsourcing.common.Exception;

import org.springframework.http.HttpStatus;

public class SuccessCode implements BaseCode {

    @Override
    public HttpStatus getStatus() {
        return null;
    }

    @Override
    public String getMessage() {
        return "";
    }
}
