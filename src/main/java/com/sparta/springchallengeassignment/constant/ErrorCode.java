package com.sparta.springchallengeassignment.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.CONFLICT;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청*/
    INVALID_VALUE(BAD_REQUEST, "값이 유효하지 않습니다."),
    INVALID_PASSWORD_CONFIRM(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    PASSWORD_CONTAINS_NICKNAME(BAD_REQUEST, "비밀번호는 닉네임을 포함할 수 없습니다."),
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰이 유효하지 않습니다."),
    MISMATCH_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰의 유저 정보가 일치하지 않습니다."),
    INVALID_EMAIL_VERIFY_AUTH_KEY(BAD_REQUEST, "이메일 인증 번호가 일치하지 않습니다."),
    TIME_OUT_EMAIL_VERIFY(BAD_REQUEST, "이메일 인증 시간이 초과했습니다."),
    MAX_IMAGES_LIMIT_OVER(BAD_REQUEST, "등록할 이미지 개수가 최대 개수를 초과했습니다."),
    INVALID_POST_FILE_URL(BAD_REQUEST, "잘못된 이미지 URL 입니다."),
    CAN_NOT_READ_IMAGE(BAD_REQUEST, "이미지를 처리할 수 없습니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자*/
    INVALID_AUTH_TOKEN(UNAUTHORIZED,"인증 정보가 없는 토큰입니다."),
    UNAUTHORIZED_MEMBER(UNAUTHORIZED,"회원 인증 정보가 존재하지 않습니다."),
    BAD_CREDENTIAL(UNAUTHORIZED,"인증 정보가 일치하지 않습니다."),

    /* 403 FORBIDDEN : 권한, 자원이 없음*/
    ACCESS_DENIED(FORBIDDEN, "권한이 없습니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    MEMBER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다."),
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "로그아웃 된 사용자입니다."),
    VALUE_NOT_FOUND(NOT_FOUND, "요청한 값을 찾을 수 없습니다."),
    POST_NOT_FOUND(NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌, 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다."),
    ALREADY_EXIST_MEMBER_NICKNAME(CONFLICT, "중복된 닉네임입니다."),
    ALREADY_EXIST_MEMBER_EMAIL(CONFLICT, "중복된 이메일입니다."),

    /* 500 INTERNAL_SERVER_ERROR : 서버 에러*/
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러입니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
