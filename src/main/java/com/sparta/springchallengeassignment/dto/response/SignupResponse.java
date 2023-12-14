package com.sparta.springchallengeassignment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignupResponse(
        @Schema(
                description = "응답 메시지",
                example = "회원 가입에 성공했습니다."
        )
        String message
) {

    public static SignupResponse of(String message) {
        return new SignupResponse(message);
    }
}
