package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomelessMainResponse {

  private Long id;
  private String name;
  private String shelterName;
  private HomelessAppSleepoverResponse planedSleepover;
}
