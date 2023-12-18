package com.sparta.springchallengeassignment.dto.response;

import com.sparta.springchallengeassignment.domain.Post;
import com.sparta.springchallengeassignment.domain.PostImage;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse (
        @Schema(description = "게시글 ID", example = "1")
        Long id,
        @Schema(description = "제목", example = "제목")
        String  title,
        @Schema(description = "내용", example = "내용")
        String content,
        @Schema(description = "작성자 아이디", example = "username1")
        String nickname,
        List<String> imageUrls,
        LocalDateTime createdAt
) {

    public static PostResponse of(Post post) {
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getMember().getNickname(), post.getImages().stream().map(PostImage::getImageUrl).toList(), post.getCreatedAt());
    }
}
