package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomelessCountResponse {

  private Long sleepoverCount;
  private Long outingCount;
  private Long totalCount;
}
