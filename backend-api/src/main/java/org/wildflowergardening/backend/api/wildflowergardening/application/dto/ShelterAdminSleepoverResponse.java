package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShelterAdminSleepoverResponse {

  private Long sleepoverId;
  private Long homelessId;
  private String homelessName;
  private LocalDate startDate;
  private LocalDate endDate;
}
