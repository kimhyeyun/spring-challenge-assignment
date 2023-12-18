package com.sparta.springchallengeassignment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

public record PostUpdateRequest(
        @NotBlank(message = "내용을 입력해주세요")
        @Size(max = 5000, message = "최대 5000자 까지 작성 가능합니다.")
         String content,

        List<String> deleteFileUrl

) {

}
