package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import lombok.Getter;

@Getter
public class HomelessExistenceRequest {

  private Long shelterId;
  private String deviceId;
  private String homelessName;
}
