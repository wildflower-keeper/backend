package org.wildflowergardening.backend.api.wildflowergardening.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateShelterPublicDeviceNameRequest {

  @NotBlank(message = "디바이스 별명이 비어있습니다.")
  @Size(max = 255, message = "디바이스 별명은 255자 이내로 입력해주세요.")
  private String deviceName;
}
