package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateShelterPublicRequest {

  @NotNull(message = "센터 id가 null입니다.")
  @Schema(description = "센터 id", example = "1")
  private Long shelterId;

  @NotBlank(message = "센터 비밀번호가 비어있습니다.")
  @Size(max = 25, min = 4, message = "센터 비밀번호는 4자 이상 25자 이하여야 합니다.")
  @Schema(description = "센터명", example = "password_example")
  private String shelterPw;

  @Size(max = 255, message = "디바이스 id는 255자 이내로 입력해주세요.")
  @Schema(description = "센터 공용 디바이스 id", example = "test_shelter_device_id")
  private String deviceId;
}
