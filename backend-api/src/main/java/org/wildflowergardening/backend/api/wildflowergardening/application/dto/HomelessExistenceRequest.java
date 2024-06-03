package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class HomelessExistenceRequest {

  @NotNull
  @Schema(example = "1")
  private Long shelterId;

  @NotEmpty
  @Schema(example = "test_device_id_1")
  private String deviceId;

  @NotEmpty
  @Schema(example = "홍길동")
  private String homelessName;
}
