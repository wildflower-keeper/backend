package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomelessResponse {

  private Long id;
  private String name;
  private String room;
  private LocalDate birthDate;
  private String phoneNumber;
}
