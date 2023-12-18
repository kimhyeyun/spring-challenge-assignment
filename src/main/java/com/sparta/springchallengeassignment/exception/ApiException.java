package com.sparta.springchallengeassignment.exception;

import com.sparta.springchallengeassignment.constant.ErrorCode;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException{

    private final ErrorCode errorCode;
    private final String message;

    public ApiException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ApiException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getDetail();
    }
}
