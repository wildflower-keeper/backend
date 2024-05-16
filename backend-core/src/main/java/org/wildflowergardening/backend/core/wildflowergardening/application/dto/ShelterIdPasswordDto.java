package org.wildflowergardening.backend.core.wildflowergardening.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShelterIdPasswordDto {

  private Long shelterId;
  private String password;
}
