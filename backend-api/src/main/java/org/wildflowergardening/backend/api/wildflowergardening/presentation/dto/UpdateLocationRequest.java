package org.wildflowergardening.backend.api.wildflowergardening.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless.LocationStatus;

@Getter
public class UpdateLocationRequest {

  @NotNull(message = "locationStatus 가 null 일 수 없습니다.")
  @Schema(description = "IN_SHELTER:재실중, OUTING:외출중", example = "IN_SHELTER")
  private LocationStatus locationStatus;
}
