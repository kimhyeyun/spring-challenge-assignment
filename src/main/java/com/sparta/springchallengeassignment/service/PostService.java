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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @Transactional
    public void deletePost(Long postId, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ApiException(POST_NOT_FOUND));
        if (!post.getMember().getId().equals(member.getId())) {
            throw new ApiException(ACCESS_DENIED);
        }

        postImageService.deleteAll(postId);
        postRepository.deleteById(postId);
    }

    public List<PostResponse> getPostList(Integer cursor, Integer size, String dir, String keyword) {
        Sort sort = Sort.by(dir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, keyword);

        Pageable pageable = PageRequest.of(cursor, size, sort);
        Page<Post> posts = postRepository.findAll(pageable);

        return posts.stream().map(PostResponse::of).toList();
    }

    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    @Transactional
    public void delete(Post post) {
        postImageService.deleteAll(post.getId());
        postRepository.deleteById(post.getId());
    }
}
