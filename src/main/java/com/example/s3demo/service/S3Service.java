package com.example.s3demo.service;

import com.example.s3demo.model.dto.PreUploadDto;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j(topic = "aws-bucket")
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.s3.url}")
    private String bucketUrl;
    private static String staticBucketUrl;

    @PostConstruct
    private void init() {
        staticBucketUrl = bucketUrl;
    }


    public String uploadFile(MultipartFile file, String prefix) throws IOException {
        String fileName = generateUniqueFileName() + getExtension(file);
        String key = prefix + "/" + fileName;

        ObjectMetadata metadata = ObjectMetadata.builder()
            .contentType(file.getContentType())
            .contentLength(file.getSize())
            .build();

        try (InputStream inputStream = file.getInputStream()) {
            s3Template.upload(bucket, key, inputStream, metadata);
        } catch (Exception e) {
            log.error("Failed to upload file. FileName : {}", fileName,e);
            throw new RuntimeException("Failed to upload file from S3", e);
        }

        return fileName;
    }

    public String getSignedUrl(String key, Duration expiration) {
        return s3Template.createSignedGetURL(bucket, key, expiration).toString();
    }

    public PreUploadDto generateSignedUrl(String prefix, String extension, Duration expiration) {
        String get = String.format("%s.%s", generateUniqueFileName(), extension);
        String put = s3Template.createSignedPutURL(bucket,
            String.format("%s/%s.%s", prefix, get , extension), expiration).toString();
        return new PreUploadDto(get, put);
    }

    public void deleteFile(String key) {
        try {
            s3Template.deleteObject(bucket, key);
        } catch (Exception e){
            log.error("Failed to delete file. Key: {}", key, e);
            throw new RuntimeException("Failed to delete file from S3", e);
        }
    }

    public boolean fileExists(String key) {
        return s3Template.objectExists(bucket, key);
    }

    public static String getFullUrl(String key) {
        return staticBucketUrl + "/" + key;
    }

    private static String generateUniqueFileName() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateString = today.format(formatter);
        return dateString + "_" + UUID.randomUUID().toString();
    }

    private static String getExtension(MultipartFile file) {
        if (file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty()) {
            return "." + FilenameUtils.getExtension(
                file.getOriginalFilename()); // Apache commons 사용 (null 시 빈 문자열 반환하여 처리)
        }
        return "";
    }

}