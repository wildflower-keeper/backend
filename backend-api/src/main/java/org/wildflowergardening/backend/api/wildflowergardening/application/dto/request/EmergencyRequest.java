package org.wildflowergardening.backend.api.wildflowergardening.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.Location;


@Getter
public class EmergencyRequest {

    @NotNull(message = "위치 정보가 null 일 수 없습니다.")
    @Schema(description = "위치 정보 (위도와 경도를 포함)")
    Location location;
}
