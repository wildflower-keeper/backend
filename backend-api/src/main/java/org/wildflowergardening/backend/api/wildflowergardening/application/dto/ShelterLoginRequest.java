package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ShelterLoginRequest {

  @NotNull(message = "센터 id가 null입니다.")
  @Schema(description = "센터 id", example = "1")
  private Long id;

  @NotEmpty(message = "센터 비밀번호가 비어있습니다.")
  @Size(max = 255, message = "센터 비밀번호를 255자 이하로 입력해주세요.")
  @Schema(description = "센터 password", example = "password_example")
  private String pw;
}
