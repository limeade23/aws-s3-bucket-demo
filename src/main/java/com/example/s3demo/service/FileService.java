package com.example.s3demo.service;

import com.example.s3demo.model.type.FileType;
import java.net.URL;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j(topic = "File")
@Service
@RequiredArgsConstructor
public class FileService {

    private final S3Service s3Service;

    public String uploadFile(MultipartFile file, FileType fileType) {
        try {
            log.info("File Upload. FileName: {}, Type: {}", file.getOriginalFilename(), fileType);
            return s3Service.uploadFile(file, fileType.getPrefix());
        } catch (Exception e) {
            log.error("Failed to upload file. FileName: {}, Type: {}", file.getOriginalFilename(), fileType, e);
            throw new RuntimeException(e);
        }
    }

    public void deleteFile(String fileName, FileType fileType) {
        try {
            log.info("File Delete. FileName: {}, Type: {}", fileName, fileType);
            s3Service.deleteFile(getKey(fileName, fileType));
        } catch (Exception e) {
            log.error("Failed to delete file. FileName: {}, Type: {}", fileName, fileType, e);
            throw new RuntimeException(e);
        }
    }

    public String getFileAccess(String fileName, FileType fileType) {
        String key = getKey(fileName, fileType);
        log.info("File Access. FileName: {}, Type: {}", fileName, fileType);

        if (!s3Service.fileExists(key)) {
            throw new RuntimeException("File Not Found");
        }

        // 파일 접근 로직 추가

        return s3Service.getSignedUrl(key, Duration.ofMinutes(3));
    }

    private static String getKey(String fileName, FileType fileType) {
        return String.format("%s/%s", fileType.getPrefix(), fileName);
    }

    public static String getFullUrl(String fileName, FileType fileType) {
        return S3Service.getFullUrl(getKey(fileName, fileType));
    }

}
