package com.sparta.springchallengeassignment.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    OK(200, "요청 성공"),
    GET_MEMBER(200, "멤버 조회 성공"),
    GET_MEMBER_POSTS(200, "해당 멤버의 게시글 목록 조회 성공"),
    GET_POST_DETAIL(200, "게시글 조회 성공"),
    UPDATE_POST(200, "게시글 수정 성공"),
    DELETE_POST(200, "게시글 삭제 성공"),
    GET_POST_LIST(200, "게시글 목록 조회 성공"),
    UPDATE_COMMENT(200, "댓글 수정 성공"),
    DELETE_COMMENT(200, "댓글 삭제 성공"),
    GET_COMMENT_LIST_BY_POST(200, "댓글 목록 조회 성공"),

    SIGN_UP(201, "회원 가입 성공"),
    LOG_IN(201, "로그인 성공"),
    CREATED_POST(201, "게시글 작성 성공"),
    CREATED_COMMENT(201, "댓글 작성 성공"),
    REISSUE_REFRESH_TOKEN(201, "리프레쉬 토큰 재발급 성공");


    private final int httpStatus;
    private final String message;
}
