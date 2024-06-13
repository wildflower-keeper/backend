package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class UpdateHomelessRequest {

  @Size(max = 255)
  @Schema(example = "김수한무")
  private String name;

  @Size(max = 255)
  @Schema(example = "B-205 호")
  private String room;

  @Schema(example = "1950-01-01", type = "string")
  private LocalDate birthDate;

  @Size(max = 50)
  @Schema(example = "01012341234")
  private String phoneNumber;

  @Schema(example = "2024-06-01", type = "string")
  private LocalDate admissionDate;
}
