package org.wildflowergardening.backend.core.wildflowergardening.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SleepoverExcelDto {

  private String homelessName;
  private String startDate;
  private String endDate;
  private String reason;
  private String emergencyContact;
  private String createdAt;
}
