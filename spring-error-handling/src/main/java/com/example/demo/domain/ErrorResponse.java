package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private final String errorCode;
    private final String message;
    private final String errorClass;

    public static ErrorResponse of(final String errorCode, final String message, final String errorClass) {
        return new ErrorResponse(errorCode, message, errorClass);
    }

    public static ErrorResponse of(final String errorCode, final Throwable error) {
        return new ErrorResponse(errorCode, error.getMessage(), error.getClass().getCanonicalName());
    }
}
