package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomelessAppSleepoverResponse {

  private Long sleepoverId;
  private LocalDate startDate;
  private LocalDate endDate;
}
