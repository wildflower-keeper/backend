package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomelessSleepoverResponse {

  private Long sleepoverId;
  private LocalDate startDate;
  private LocalDate endDate;
  private String reason;
  private boolean isCancelable;
}
