package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomelessPageRequest {

  private HomelessFilterType filterType;
  private String filterValue;
  private Long shelterId;
  private int pageNumber;
  private int pageSize;
  private LocalDateTime targetDateTime;
}
