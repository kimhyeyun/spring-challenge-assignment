package com.sparta.springchallengeassignment.dto.request;

import com.sparta.springchallengeassignment.domain.Member;
import com.sparta.springchallengeassignment.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "게시글 생성 요청")
public record PostRequest (
        @NotBlank(message = "제목을 입력해주세요")
        @Size(max = 500, message = "최대 500자 까지 작성 가능합니다.")
        String title,


        @NotBlank(message = "내용을 입력해주세요.")
        @Size(max = 5000, message = "최대 5000자 까지 작성 가능합니다.")
        String content

){
    public Post toEntity(Member member) {
        return Post.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
    }
}
