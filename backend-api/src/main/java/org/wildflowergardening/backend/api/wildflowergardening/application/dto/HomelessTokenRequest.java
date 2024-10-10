package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class HomelessTokenRequest {

  @NotNull
  @Schema(example = "1")
  private Long shelterId;

  @NotNull
  @Schema(example = "1234")
  private String shelterPin;

  @NotEmpty
  @Schema(example = "홍길동")
  private String homelessName;

  @Schema(example = "test_device_id_1(생략가능)")
  private String deviceId;

  @NotNull
  @Schema(example = "B동303호")
  private String room;

  @Size(max = 50)
  @Schema(example = "01012341234(생략가능)")
  private String phoneNumber;
}
