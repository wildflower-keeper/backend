package org.wildflowergardening.backend.api.wildflowergardening.presentation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomelessIdNameResponse {

  private Long id;
  private String name;
}
