package com.sparta.springchallengeassignment.exception;

public class InvalidPasswordConfirmation extends ApiException {
    public InvalidPasswordConfirmation() {
        super(ErrorCode.INVALID_PASSWORD_CONFIRM);
    }
}
