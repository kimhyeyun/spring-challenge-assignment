package com.sparta.springchallengeassignment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse (
        @Schema(description = "응답 메세지")
        String message
){

    public static LoginResponse of() {
        return new LoginResponse("로그인에 성공했습니다.");
    }
}
