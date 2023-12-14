package com.sparta.springchallengeassignment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "회원 가입 요청")
public record SignupRequest (
        @Schema(description = "닉네임" , example = "yuni")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "닉네임은 영어, 숫자의 조합으로 입력해야 합니다.")
        @Size(min = 3, message = "닉네임은 최소 3자 이상 입력해야 합니다.")
        String nickname,

        @Schema(description = "비밀번호", example = "password1234")
        @Size(min = 4, message = "비밀번호는 최소 4자 이상 입력해야 합니다.")
        String password,

        @Schema(description = "비밀번호 확인", example = "password1234")
        String confirm_password,

        @Schema(description = "이메일", example = "test@email.com")
        @Email
        String email

) {

}
