package com.example.s3demo.controller;

import com.example.s3demo.model.request.ImagePreUploadRequest;
import com.example.s3demo.model.request.ImageUploadRequest;
import com.example.s3demo.service.SampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    @PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity imageUpload(
        @RequestPart("file") MultipartFile file,
        @RequestPart("request") ImageUploadRequest request) {
        return ResponseEntity.ok(sampleService.uploadImage(file, request));
    }

    @PostMapping("/image/pre-upload")
    public ResponseEntity imagePreUpload(
        @RequestBody ImagePreUploadRequest request) {
        return ResponseEntity.ok(sampleService.uploadPreImage(request));
    }

}
