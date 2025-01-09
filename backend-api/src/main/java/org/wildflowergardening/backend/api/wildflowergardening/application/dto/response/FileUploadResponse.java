package org.wildflowergardening.backend.api.wildflowergardening.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileUploadResponse {
    private String imageUrl;
}
