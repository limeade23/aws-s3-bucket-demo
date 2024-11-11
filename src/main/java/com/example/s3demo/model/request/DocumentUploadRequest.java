package com.example.s3demo.model.request;

import jakarta.validation.constraints.NotBlank;

public record DocumentUploadRequest(
    @NotBlank
    String description,

    @NotBlank
    String password
) { }
