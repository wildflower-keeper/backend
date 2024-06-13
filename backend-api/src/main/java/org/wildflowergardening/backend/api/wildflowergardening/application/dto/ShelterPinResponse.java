package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShelterPinResponse {

  @Schema(description = "네자리 숫자", example = "1234")
  private String pin;

  @Schema(description = "만료일시")
  private LocalDateTime expiredAt;
}
