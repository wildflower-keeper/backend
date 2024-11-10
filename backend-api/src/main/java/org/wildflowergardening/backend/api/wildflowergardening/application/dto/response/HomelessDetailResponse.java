package org.wildflowergardening.backend.api.wildflowergardening.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessResponse;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.InOutStatus;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationTracking;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class HomelessDetailResponse {
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
    private InOutStatus lastLocationStatus;

    @Schema(description = "마지막 위치 확인 일시")
    private LocalDateTime lastLocationTrackedAt;

    @Schema(description = "노숙인 전화번호", example = "01012341234")
    private String phoneNumber;

    @Schema(description = "센터 입소일", example = "2023-01-01")
    private LocalDate admissionDate;

    @Schema(description = "메모", example = "복숭아 알러지")
    private String memo;

    @Schema(description = "외박 기간", example = "2024-05-23")
    private String sleepoverDate;

    @Schema(description = "외박 사유", example = "쉼터 공원")
    private String description;

    @Schema(description = "비상 연락망", example = "비상연락망")
    private String emergencyPhoneNumber;

    public static HomelessDetailResponse from(
            Homeless homeless, LocationTracking lastLocationTracking, boolean targetDateSleepover
    ) {
        InOutStatus locationStatus =
                lastLocationTracking != null ? lastLocationTracking.getInOutStatus() : null;
        LocalDateTime lastLocationTrackedAt =
                lastLocationTracking != null ? lastLocationTracking.getTrackedAt() : null;

        return HomelessDetailResponse.builder()
                .id(homeless.getId())
                .name(homeless.getName())
                .room(homeless.getRoom())
                .targetDateSleepover(targetDateSleepover)
                .birthDate(homeless.getBirthDate())
                .phoneNumber(homeless.getPhoneNumber())
                .admissionDate(homeless.getAdmissionDate())
                .lastLocationStatus(locationStatus)
                .lastLocationTrackedAt(lastLocationTrackedAt)
                .memo(homeless.getMemo())
                .build();
    }
}
