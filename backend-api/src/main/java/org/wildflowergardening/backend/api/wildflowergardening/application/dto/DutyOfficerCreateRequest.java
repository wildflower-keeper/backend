package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class DutyOfficerCreateRequest {

  @NotEmpty
  @Size(max = 100)
  @Schema(example = "김담당자")
  private String name;

  @NotEmpty
  @Size(max = 50)
  @Schema(example = "01011110000")
  private String phoneNumber;

  @NotNull
  @Schema(example = "2024-07-01", type = "string")
  private LocalDate targetDate;
}
