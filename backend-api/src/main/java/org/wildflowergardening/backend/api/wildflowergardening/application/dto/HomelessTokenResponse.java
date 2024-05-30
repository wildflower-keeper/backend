package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomelessTokenResponse {

  private Long homelessId;
  private String accessToken;
}
