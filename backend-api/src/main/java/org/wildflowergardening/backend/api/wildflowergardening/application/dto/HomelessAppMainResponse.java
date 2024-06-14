package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomelessAppMainResponse {

  private Long id;
  private String homelessName;
  private Long shelterId;
  private String shelterName;
  private LocalDateTime targetDateTime;
  private boolean yesterdaySleepoverExists;
  private boolean todaySleepoverExists;
  private boolean futureSleepoverExists;
}
