package org.wildflowergardening.backend.api.wildflowergardening.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateChiefOfficerRequest {

  @Schema(example = "새로운 책임자 성함")
  @Size(max = 100)
  private String name;

  @Schema(example = "01043214321")
  @Size(max = 50)
  private String phoneNumber;
}
