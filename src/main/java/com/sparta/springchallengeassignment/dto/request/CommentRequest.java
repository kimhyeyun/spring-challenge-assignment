package com.sparta.springchallengeassignment.dto.request;

import com.sparta.springchallengeassignment.domain.Comment;
import com.sparta.springchallengeassignment.domain.Member;
import com.sparta.springchallengeassignment.domain.Post;
import jakarta.validation.constraints.NotBlank;

public record CommentRequest(
        @NotBlank(message = "내용을 입력 해주세요.")
        String content
) {

        public Comment toEntity(Post post, Member member) {
                return Comment.builder()
                        .content(content)
                        .post(post)
                        .member(member)
                        .build();
        }

}
