package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomelessCountResponse {

  @Schema(description = "센터의 모든 노숙인 수")
  private Long totalHomelessCount;

  @Schema(description = "외박 신청 조회 기준일")
  private LocalDate sleepoverTargetDate;

  @Schema(description = "sleepoverTargetDate 기준 외박 신청자 수")
  private Long sleepoverCount;

  @Schema(description = "위치 추적 데이터 조회 기준일"
      + " (locationTrackedAfter 보다 늦게 발생한 위치 추적 데이터만 조회)")
  private LocalDateTime locationTrackedAfter;

  @Schema(description = "위치 추적 데이터가 존재하는 노숙인 수")
  private Long locationTrackedHomelessCount;

  @Schema(description = "외출중으로 감지된 노숙인 수")
  private Long outingCount;

  @Schema(description = "재실중으로 감지된 노숙인 수")
  private Long inShelterCount;
}
