package org.wildflowergardening.backend.api.wildflowergardening.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless.LocationStatus;

@Getter
@Builder
public class HomelessResponse {

  private Long id;

  @Schema(description = "노숙인 성함", example = "홍길동")
  private String name;

  @Schema(description = "방 번호", example = "201호실")
  private String room;

  @Schema(description = "노숙인 생년월일", example = "1970-01-01")
  private LocalDate birthDate;

  @Schema(description = "targetDate 외박 신청 여부", example = "false")
  private Boolean targetDateSleepover;

  @Schema(description = "마지막 위치 상태")
  private LocationStatus lastLocationStatus;

  @Schema(description = "마지막 위치 확인 일시")
  private LocalDateTime lastLocationTrackedAt;

  @Schema(description = "노숙인 전화번호", example = "010-0000-0000")
  private String phoneNumber;

  @Schema(description = "센터 입소일", example = "2023-01-01")
  private LocalDate admissionDate;

  public static HomelessResponse from(Homeless homeless, boolean targetDateSleepover) {
    return HomelessResponse.builder()
        .id(homeless.getId())
        .name(homeless.getName())
        .room(homeless.getRoom())
        .targetDateSleepover(targetDateSleepover)
        .birthDate(homeless.getBirthDate())
        .phoneNumber(homeless.getPhoneNumber())
        .admissionDate(homeless.getAdmissionDate())
        .lastLocationStatus(homeless.getLastLocationStatus())
        .lastLocationTrackedAt(homeless.getLastLocationTrackedAt())
        .build();
  }
}
