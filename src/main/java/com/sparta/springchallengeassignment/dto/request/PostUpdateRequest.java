package com.sparta.springchallengeassignment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class PostUpdateRequest {

    @NotBlank(message = "내용을 입력해주세요")
    @Size(max = 5000, message = "최대 5000자 까지 작성 가능합니다.")
    private String content;

    private List<String> deleteFileUrl;
}
