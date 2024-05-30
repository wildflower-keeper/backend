package org.wildflowergardening.backend.core.wildflowergardening.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LocationTrackingStatisticDto {

  private Long trackedHomelessCount;
  private Long inShelterCount;
  private Long outingCount;
}
