package com.example.outsourcing.common.response;


import com.example.outsourcing.common.exception.BaseCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp = LocalDateTime.now();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<FieldError> fieldErrors;


    public static ErrorResponse of(BaseCode baseCode) {
        return ErrorResponse.builder()
                .fieldErrors(new ArrayList<>())
                .build();
    }

    public static ErrorResponse of(BaseCode baseCode, String message) {
        return ErrorResponse.builder()
                .fieldErrors(new ArrayList<>())
                .build();
    }

    public static ErrorResponse of(BaseCode baseCode, List<FieldError> fieldErrors) {
        return ErrorResponse.builder()
                .fieldErrors(fieldErrors)
                .build();
    }



    @Getter
    @Builder
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        public static FieldError of(String field, String value, String reason) {
            return FieldError.builder()
                    .field(field)
                    .value(value)
                    .reason(reason)
                    .build();
        }
    }

}