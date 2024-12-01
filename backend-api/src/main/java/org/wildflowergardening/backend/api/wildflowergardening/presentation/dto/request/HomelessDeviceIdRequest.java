package org.wildflowergardening.backend.api.wildflowergardening.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class HomelessDeviceIdRequest {
    @NotEmpty(message = "device id가 비어 있습니다")
    @Schema(description = "device_id", example = "deviceId_example")
    private String deviceId;
}
