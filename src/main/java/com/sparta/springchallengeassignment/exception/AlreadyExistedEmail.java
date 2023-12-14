package com.sparta.springchallengeassignment.exception;

public class AlreadyExistedEmail extends ApiException {
    public AlreadyExistedEmail() {
        super(ErrorCode.ALREADY_EXIST_MEMBER_EMAIL);
    }
}
