package com.sparta.springchallengeassignment.service;

import com.sparta.springchallengeassignment.constant.ErrorCode;
import com.sparta.springchallengeassignment.domain.Like;
import com.sparta.springchallengeassignment.domain.Member;
import com.sparta.springchallengeassignment.domain.Post;
import com.sparta.springchallengeassignment.dto.response.LikeByMemberResponse;
import com.sparta.springchallengeassignment.dto.response.LikeResponse;
import com.sparta.springchallengeassignment.exception.ApiException;
import com.sparta.springchallengeassignment.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberService memberService;
    private final PostService postService;

    @Transactional
    public LikeResponse likePost(Long memberId, Long postId, Member member) {
        if (!memberId.equals(member.getId())) {
            throw new ApiException(ErrorCode.ACCESS_DENIED);
        }
        Member findMember = memberService.findMember(memberId);
        Post findPost = postService.findPost(postId);

        if (likeRepository.existsByMember_IdAndPost_Id(findMember.getId(), findPost.getId())) {
            throw new ApiException(ErrorCode.ALREADY_EXIST_LIKE);
        }

        Like like = likeRepository.save(Like.builder().member(findMember).post(findPost).build());
        return LikeResponse.of(like);
    }

    @Transactional
    public void dislikePost(Long memberId, Long postId, Member member) {
        if (!memberId.equals(member.getId())) {
            throw new ApiException(ErrorCode.ACCESS_DENIED);
        }
        Member findMember = memberService.findMember(memberId);
        Post findPost = postService.findPost(postId);


        Like like = likeRepository.findByMember_IdAndPost_Id(findMember.getId(), findPost.getId()).orElseThrow(() ->
                new ApiException(ErrorCode.NON_EXIST_LIKE)
        );

        likeRepository.delete(like);
    }

    public LikeByMemberResponse getLikesByMember(Long memberId, Member member) {
        if (!memberId.equals(member.getId())) {
            throw new ApiException(ErrorCode.ACCESS_DENIED);
        }
        Member findMember = memberService.findMember(memberId);

        List<Like> likes = likeRepository.findAllByMember_Id(findMember.getId());
        return LikeByMemberResponse.of(likes);
    }
}
