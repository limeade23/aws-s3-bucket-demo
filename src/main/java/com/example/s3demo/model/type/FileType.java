package com.example.s3demo.model.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {
    IMAGE("images"),
    DOCUMENT("documents")
    ;

    private final String prefix;
}
