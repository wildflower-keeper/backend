package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShelterAdminSleepoverResponse {

  @Schema(description = "외박 신청 id")
  private Long sleepoverId;

  @Schema(description = "노숙인 id")
  private Long homelessId;

  @Schema(description = "노숙인 성함")
  private String homelessName;

  @Schema(description = "노숙인 방번호")
  private String homelessRoom;

  @Schema(description = "노숙인 연락처")
  private String homelessPhoneNumber;

  @Schema(description = "비상연락망")
  private String emergencyContact;

  @Schema(description = "외박사유")
  private String reason;

  @Schema(description = "외박 시작일")
  private LocalDate startDate;

  @Schema(description = "외박 종료일")
  private LocalDate endDate;

  @Schema(description = "외박신청일시")
  private LocalDateTime createdAt;
}
