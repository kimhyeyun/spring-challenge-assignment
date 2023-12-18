package com.sparta.springchallengeassignment.exception;

import com.sparta.springchallengeassignment.constant.ErrorCode;

public class InvalidPasswordConfirmation extends ApiException {
    public InvalidPasswordConfirmation() {
        super(ErrorCode.INVALID_PASSWORD_CONFIRM);
    }
}
