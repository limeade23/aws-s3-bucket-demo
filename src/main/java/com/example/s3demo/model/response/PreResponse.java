package com.example.s3demo.model.response;

public record PreResponse(
    String presignedGetUrl,
    String presignedPutUrl
) { }
