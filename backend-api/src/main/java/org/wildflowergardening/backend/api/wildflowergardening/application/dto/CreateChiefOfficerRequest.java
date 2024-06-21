package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateChiefOfficerRequest {

  @Schema(description = "책임자 성함", example = "양병주")
  @Size(max = 100)
  @NotEmpty
  private String name;

  @Schema(description = "책임자 전화번호", example = "01012341234")
  @Size(max = 50)
  @NotEmpty
  private String phoneNumber;
}
