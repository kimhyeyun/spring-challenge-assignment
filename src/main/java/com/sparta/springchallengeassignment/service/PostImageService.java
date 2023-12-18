package com.sparta.springchallengeassignment.service;

import com.sparta.springchallengeassignment.domain.Post;
import com.sparta.springchallengeassignment.domain.PostImage;
import com.sparta.springchallengeassignment.repository.PostImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostImageService {

    private final PostImageRepository postImageRepository;
    private final S3Service s3Service;

    public final static String POST_PATH_PREFIX = "posts/";


    @Transactional(propagation = Propagation.MANDATORY)
    public void uploadImages(Post post, MultipartFile[] images) throws IOException {
        for (MultipartFile image : images) {
            String imageUrl = s3Service.uploadMultipartFileWithPublicRead(
                    POST_PATH_PREFIX + post.getId().toString() + "/",
                    image
            );

            PostImage postImage = new PostImage(post, imageUrl);
            postImageRepository.save(postImage);
        }
    }
}
