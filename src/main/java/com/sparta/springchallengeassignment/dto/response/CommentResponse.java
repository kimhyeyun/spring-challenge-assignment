package com.sparta.springchallengeassignment.dto.response;

import com.sparta.springchallengeassignment.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record CommentResponse(
        @Schema(description = "댓글 ID")
        Long id,
        @Schema(description = "댓글 내용")
        String content,
        @Schema(description = "작성자")
        String nickname,
        @Schema(description = "게시글 ID")
        Long postId,

        @Schema(description = "작성일")
        LocalDateTime createdAt
) {

    public static CommentResponse of(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getContent(), comment.getMember().getNickname(), comment.getPost().getId(), comment.getCreatedAt());
    }
}
