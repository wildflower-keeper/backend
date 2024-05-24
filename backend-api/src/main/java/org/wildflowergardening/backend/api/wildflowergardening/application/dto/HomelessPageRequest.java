package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomelessPageRequest {

  private Long shelterId;
  private int pageNumber;
  private int pageSize;
  private LocalDate targetDay;
}
