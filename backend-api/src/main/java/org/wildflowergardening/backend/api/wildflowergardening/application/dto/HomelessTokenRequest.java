package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class HomelessTokenRequest {

  @NotNull
  private Long homelessId;

  @NotNull
  private Long shelterId;

  @NotEmpty
  private String homelessName;

  @NotEmpty
  private String deviceId;
}
