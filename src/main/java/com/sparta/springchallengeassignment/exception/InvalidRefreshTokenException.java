package com.sparta.springchallengeassignment.exception;

public class InvalidRefreshTokenException extends ApiException {

    public InvalidRefreshTokenException() {
        super(ErrorCode.INVALID_REFRESH_TOKEN);

    }
}
