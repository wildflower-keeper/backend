package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomelessExistenceResponse {

  private boolean exist;
  private Long homelessId;
}
