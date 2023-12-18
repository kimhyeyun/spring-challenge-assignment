package com.sparta.springchallengeassignment.exception;

import com.sparta.springchallengeassignment.constant.ErrorCode;

public class InvalidEmailVerify extends ApiException {
    public InvalidEmailVerify() {
        super(ErrorCode.INVALID_EMAIL_VERIFY_AUTH_KEY);
    }
}
