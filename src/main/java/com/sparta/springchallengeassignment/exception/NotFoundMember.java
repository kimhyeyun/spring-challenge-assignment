package com.sparta.springchallengeassignment.exception;

public class NotFoundMember extends ApiException {

    public NotFoundMember() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
