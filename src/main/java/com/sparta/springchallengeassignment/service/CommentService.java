package com.sparta.springchallengeassignment.service;

import com.sparta.springchallengeassignment.constant.ErrorCode;
import com.sparta.springchallengeassignment.domain.Comment;
import com.sparta.springchallengeassignment.domain.Member;
import com.sparta.springchallengeassignment.domain.Post;
import com.sparta.springchallengeassignment.dto.request.CommentRequest;
import com.sparta.springchallengeassignment.dto.response.CommentResponse;
import com.sparta.springchallengeassignment.exception.ApiException;
import com.sparta.springchallengeassignment.repository.CommentRepository;
import com.sparta.springchallengeassignment.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    @Transactional
    public CommentResponse createComment(Long postId, CommentRequest request, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ApiException(ErrorCode.POST_NOT_FOUND));

        Comment comment = request.toEntity(post, member);
        post.addComment(comment);
        commentRepository.save(comment);

        return CommentResponse.of(comment);
    }

    @Transactional
    public CommentResponse updateComment(Long postId, Long commentId, CommentRequest request, Member member) {
        Comment comment = findPostAndComment(postId, commentId, member);

        comment.update(request);
        return CommentResponse.of(comment);
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId, Member member) {
        Comment comment = findPostAndComment(postId, commentId, member);
        commentRepository.delete(comment);
    }

    private Comment findPostAndComment(Long postId, Long commentId, Member member) {
        postRepository.findById(postId).orElseThrow(() -> new ApiException(ErrorCode.POST_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ApiException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getMember().getId().equals(member.getId())) {
            throw new ApiException(ErrorCode.ACCESS_DENIED);
        }
        return comment;
    }

    @Transactional
    public void deleteComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ApiException(ErrorCode.POST_NOT_FOUND));
        List<Comment> comments = post.getComments();

        commentRepository.deleteAll(comments);
    }

    public List<CommentResponse> getCommentList(Long postId, Integer cursor, Integer size, String dir, String keyword) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ApiException(ErrorCode.POST_NOT_FOUND));
        Sort sort = Sort.by(dir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, keyword);

        Pageable pageable = PageRequest.of(cursor, size, sort);
        Page<Comment> comments = commentRepository.findAllByPost(pageable, post);

        return comments.stream().map(CommentResponse::of).toList();
    }
}
