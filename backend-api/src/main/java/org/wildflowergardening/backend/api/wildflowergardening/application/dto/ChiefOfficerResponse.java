package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChiefOfficerResponse {

  private Long chiefOfficerId;
  private String name;
  private String phoneNumber;
}
