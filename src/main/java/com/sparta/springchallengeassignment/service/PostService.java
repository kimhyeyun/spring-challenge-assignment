package com.sparta.springchallengeassignment.service;

import com.sparta.springchallengeassignment.constant.ErrorCode;
import com.sparta.springchallengeassignment.domain.Member;
import com.sparta.springchallengeassignment.domain.Post;
import com.sparta.springchallengeassignment.dto.request.PostRequest;
import com.sparta.springchallengeassignment.dto.request.PostUpdateRequest;
import com.sparta.springchallengeassignment.dto.response.PostResponse;
import com.sparta.springchallengeassignment.exception.ApiException;
import com.sparta.springchallengeassignment.repository.MemberRepository;
import com.sparta.springchallengeassignment.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.sparta.springchallengeassignment.constant.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final static short MAX_IMAGES = 4;

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostImageService postImageService;

    @Transactional
    public PostResponse createPost(PostRequest request, Member member, MultipartFile[] images) {
        if (images.length > MAX_IMAGES) throw new ApiException(ErrorCode.MAX_IMAGES_LIMIT_OVER);

        Post post = request.toEntity(member);
        Post savedPost = postRepository.save(post);

        try {
            postImageService.uploadImages(savedPost, images);
        } catch (Exception e) {
            throw new ApiException(CAN_NOT_READ_IMAGE);
        }
        return PostResponse.of(savedPost);
    }

    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ApiException(POST_NOT_FOUND));

        return PostResponse.of(post);
    }

    @Transactional
    public PostResponse updatePost(PostUpdateRequest request, Long postId, Member member, MultipartFile[] images) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ApiException(POST_NOT_FOUND));
        if (!post.getMember().getId().equals(member.getId())) {
            throw new ApiException(ACCESS_DENIED);
        }

        if (post.getImages().size() + images.length - request.getDeleteFileUrl().size() > MAX_IMAGES) {
            throw new ApiException(MAX_IMAGES_LIMIT_OVER);
        }

        post.update(request);

        try {
            postImageService.uploadImages(post, images);
        } catch (Exception e) {
            throw new ApiException(CAN_NOT_READ_IMAGE);
        }

        try {
            for (String url : request.getDeleteFileUrl()) {
                postImageService.deleteImage(post, url);
            }
        } catch (Exception e) {
            throw new ApiException(INTERNAL_SERVER_ERROR);
        }

        return PostResponse.of(post);
    }
}
