package com.example.s3demo.model.response;

import com.example.s3demo.model.entity.Image;
import com.example.s3demo.model.type.FileType;
import com.example.s3demo.service.FileService;

public record ImageResponse(
    Long id,
    String url,
    String fileName,
    String description,
    String email
) {
    public static ImageResponse of(Image image) {
        return new ImageResponse(
            image.getId(),
            FileService.getFullUrl(image.getFile().getStoredName(), FileType.IMAGE),
            image.getFile().getOriginalName(),
            image.getDescription(),
            image.getEmail()
        );
    }
}
