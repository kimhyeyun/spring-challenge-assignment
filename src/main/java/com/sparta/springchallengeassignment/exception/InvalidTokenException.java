package com.sparta.springchallengeassignment.exception;

import com.sparta.springchallengeassignment.constant.ErrorCode;

public class InvalidTokenException extends ApiException {

    public InvalidTokenException() {
        super(ErrorCode.INVALID_AUTH_TOKEN);

    }
}
