package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DutyOfficerResponse {

  private Long dutyOfficerId;
  private String name;
  private String phoneNumber;
  private LocalDate targetDate;
}
