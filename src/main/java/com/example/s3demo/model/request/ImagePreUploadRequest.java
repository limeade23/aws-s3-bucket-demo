package com.example.s3demo.model.request;

public record ImagePreUploadRequest(
    String email,
    String description,
    String expression,
    String originalName
) {}
