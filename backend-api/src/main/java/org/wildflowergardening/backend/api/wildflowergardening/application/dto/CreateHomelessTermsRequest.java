package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateHomelessTermsRequest {

  @Schema(example = "위치정보 수집 동의")
  @NotEmpty
  private String title;

  @Schema(example = "blablablablablablablablablablablablablablablablablablablablablablablabla")
  @NotEmpty
  @Size(max = 5_000)
  private String detail;

  @Schema(example = "true")
  @NotNull
  private Boolean isEssential;
}
