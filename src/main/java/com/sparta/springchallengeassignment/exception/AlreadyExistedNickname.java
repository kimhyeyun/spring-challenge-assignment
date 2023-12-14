package com.sparta.springchallengeassignment.exception;

public class AlreadyExistedNickname extends ApiException {

    public AlreadyExistedNickname() {
        super(ErrorCode.ALREADY_EXIST_MEMBER_NICKNAME);
    }
}
