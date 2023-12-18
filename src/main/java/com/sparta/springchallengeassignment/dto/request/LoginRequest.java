package com.sparta.springchallengeassignment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


public record LoginRequest (
        @Schema(description = "로그인 닉네임", example = "yuni")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "닉네임은 영어, 숫자의 조합으로 입력해야 합니다.")
        @Size(min = 3, message = "닉네임은 최소 3자 이상 입력해야 합니다.")
        String nickname,

        @Schema(description = "비밀번호", example = "password1234")
        @Size(min = 4, message = "비밀번호는 최소 4자 이상 입력해야 합니다.")
        String password
){


}
