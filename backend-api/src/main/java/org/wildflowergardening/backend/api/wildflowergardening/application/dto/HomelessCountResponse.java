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

  private SleepoverCount sleepoverCount;

  private LocationTrackingCount locationTrackingCount;

  private EmergencyCount emergencyCount;

  @Getter
  @Builder
  public static class SleepoverCount {

    @Schema(description = "외박 신청 조회 기준일")
    private LocalDate targetDate;

    @Schema(description = "sleepoverTargetDate 기준 외박 신청자 수")
    private Long count;
  }

  @Getter
  @Builder
  public static class LocationTrackingCount {

    @Schema(description = "위치 추적 데이터 조회 기준일시"
        + " (locationTrackedAfter 보다 늦게 발생한 위치 추적 데이터만 조회)")
    private LocalDateTime locationTrackedAfter;

    @Schema(description = "locationTrackedAfter 이후에 위치 추적 데이터가 존재하는 노숙인 수")
    private Long locationTrackedHomelessCount;

    @Schema(description = "외출중으로 감지된 노숙인 수")
    private Long outingCount;

    @Schema(description = "재실중으로 감지된 노숙인 수")
    private Long inShelterCount;
  }

  @Getter
  @Builder
  public static class EmergencyCount {

    @Schema(
        description = "긴급상황 발생목록 조회 기준일시"
            + " (emergencyOccurredAfter 보다 늦게 발생한 긴급상황 데이터만 조회)"
    )
    private LocalDateTime emergencyOccurredAfter;

    @Schema(description = "긴급상황 발생 건수", example = "0")
    private Long count;
  }
}
