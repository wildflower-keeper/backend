package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomelessTermsResponse {

  private Long id;

  @Schema(description = "약관명")
  private String title;

  @Schema(description = "약관 내용")
  private String detail;

  @Schema(description = "필수 여부")
  private Boolean isEssential;
}
