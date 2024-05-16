package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ShelterLoginRequest {

  @Schema(description = "센터 id", example = "1")
  private Long id;

  @Schema(description = "센터 관리자 password", example = "password_example")
  private String pw;
}
