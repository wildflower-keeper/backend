package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomelessAppMainResponse {

  private Long id;
  private String homelessName;
  private Long shelterId;
  private String shelterName;
  private UpcomingSleepover upcomingSleepover;

  @Getter
  @Builder
  public static class UpcomingSleepover {

    private Long sleepoverId;

    @Schema(description = "외박시작일")
    private LocalDate startDate;

    @Schema(description = "외박종료일")
    private LocalDate endDate;

    @Schema(description = "외박일수")
    private int nightCount;

    @Schema(description = "상태 (IN_PROGRESS:진행중, TODAY_SCHEDULE:오늘예정, FUTURE_SCHEDULED:미래예정)")
    private UpcomingSleepoverStatus status;

    public enum UpcomingSleepoverStatus {
      IN_PROGRESS, TODAY_SCHEDULED, FUTURE_SCHEDULED
    }
  }
}
