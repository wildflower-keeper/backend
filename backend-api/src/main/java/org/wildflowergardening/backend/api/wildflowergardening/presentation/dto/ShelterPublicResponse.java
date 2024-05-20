package org.wildflowergardening.backend.api.wildflowergardening.presentation.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ShelterPublicResponse {

  private Long shelterPublicId;
  private Long shelterId;
  private String shelterName;
  private String deviceName;
}
