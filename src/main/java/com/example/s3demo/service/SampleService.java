package com.example.s3demo.service;

import com.example.s3demo.model.dto.PreUploadDto;
import com.example.s3demo.model.entity.File;
import com.example.s3demo.model.entity.Image;
import com.example.s3demo.model.request.ImagePreUploadRequest;
import com.example.s3demo.model.request.ImageUploadRequest;
import com.example.s3demo.model.response.ImageResponse;
import com.example.s3demo.model.response.PreResponse;
import com.example.s3demo.model.type.FileType;
import com.example.s3demo.repository.DocumentRepository;
import com.example.s3demo.repository.ImageRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final FileService fileService;
    private final S3Service s3Service;

    private final DocumentRepository documentRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public ImageResponse uploadImage(MultipartFile file, ImageUploadRequest request) {
        String storedName = fileService.uploadFile(file, FileType.IMAGE);

        Image stored = Image.builder()
            .email(request.email())
            .description(request.description())
            .file(
                File.builder()
                    .originalName(file.getOriginalFilename())
                    .storedName(storedName)
                    .build()
            )
            .build();

        stored = imageRepository.save(stored);

        return ImageResponse.of(stored);
    }

    @Transactional
    public PreResponse uploadPreImage(ImagePreUploadRequest request) {
        PreUploadDto upload = s3Service.generateSignedUrl(FileType.IMAGE.getPrefix(),
            request.expression(), Duration.ofMinutes(5));

        Image stored = Image.builder()
            .email(request.email())
            .description(request.description())
            .file(
                File.builder()
                    .originalName(request.originalName())
                    .storedName(upload.get())
                    .build()
            )
            .build();

        imageRepository.save(stored);

        return new PreResponse(upload.get(), upload.put());
    }




}
