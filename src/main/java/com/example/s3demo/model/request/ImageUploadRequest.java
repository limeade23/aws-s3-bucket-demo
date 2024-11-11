package com.example.s3demo.model.request;

public record ImageUploadRequest(
    String email,
    String description
) {}
