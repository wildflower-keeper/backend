package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationStatus;

@Getter
public class UpdateLocationRequest {

  @NotNull(message = "locationStatus 가 null 일 수 없습니다.")
  @Schema(description = "IN_SHELTER:재실중, OUTING:외출중", example = "IN_SHELTER")
  private LocationStatus locationStatus;

  @Schema(description = "위치 확인 일시", example = "2024-06-03 00:00:01.123123", type = "string")
  private LocalDateTime trackedAt;
}
