package org.wildflowergardening.backend.core.wildflowergardening.application.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;

@Getter
@Builder
public class CreateSleepoverDto {

  private UserRole creatorType;
  private Long shelterId;
  private Long homelessId;
  private LocalDate startDate;
  private LocalDate endDate;
  private String reason;
  private String emergencyContact;
}
