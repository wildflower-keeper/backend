package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class CreateHomelessTermsRequest {

  @Schema(example = "위치정보 수집 동의", description = "약관명")
  @NotEmpty
  private String title;

  @Schema(example = "blablablablablablablablablablablablablablablablablablablablablablablabla", description = "약관내용")
  @NotEmpty
  @Size(max = 5_000)
  private String detail;

  @Schema(example = "true", description = "필수 여부")
  @NotNull
  private Boolean isEssential;

  @Schema(example = "2024-06-01", description = "적용 시작일")
  @NotNull
  private LocalDate startDate;
}
