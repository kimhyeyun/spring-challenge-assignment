package com.sparta.springchallengeassignment.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.sparta.springchallengeassignment.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sparta.springchallengeassignment.constant.ErrorCode.INVALID_POST_FILE_URL;
import static com.sparta.springchallengeassignment.service.PostImageService.POST_PATH_PREFIX;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String uploadMultipartFileWithPublicRead(String prefix, MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String fileNameExtension = StringUtils.getFilenameExtension(fileName);

        fileName = prefix + UUID.randomUUID() + "." + fileNameExtension;
        String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());

        PutObjectRequest request = new PutObjectRequest(
                bucket, fileName, multipartFile.getInputStream(), metadata
        )
                .withCannedAcl(CannedAccessControlList.PublicRead);

        return fileUrl;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void deletePostFile(String fileUrl) {
        amazonS3Client.deleteObject(bucket, extractKey(fileUrl));
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void deleteAllPostFiles(String postId) {
        ListObjectsRequest request = new ListObjectsRequest()
                .withBucketName(bucket)
                .withPrefix(POST_PATH_PREFIX + postId + "/");

        List<S3ObjectSummary> objectSummaries = amazonS3Client.listObjects(request).getObjectSummaries();

        for (S3ObjectSummary summary : objectSummaries) {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, summary.getKey()));
        }
    }

    private String extractKey(String fileUrl) {
        String regex = "(posts/.*)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileUrl);

        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new ApiException(INVALID_POST_FILE_URL);
    }
}
