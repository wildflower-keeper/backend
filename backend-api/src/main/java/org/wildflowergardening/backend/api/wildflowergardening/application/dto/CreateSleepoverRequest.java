package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateSleepoverRequest {

  @NotNull
  @Schema(description = "외박 시작일", example = "2024-08-01")
  private LocalDate startDate;

  @NotNull
  @Schema(description = "외박 종료일", example = "2024-08-02")
  private LocalDate endDate;
}
