package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateSleepoverRequest {

  @NotNull
  @Schema(description = "외박 시작일", example = "2024-06-29")
  private LocalDate startDate;

  @NotNull
  @Schema(description = "외박 종료일", example = "2024-06-30")
  private LocalDate endDate;

  @Schema(description = "외박사유", example = "")
  @Size(max = 255)
  private String reason;

  @Schema(description = "비상연락처", example = "")
  @Size(max = 255)
  private String emergencyContact;
}
