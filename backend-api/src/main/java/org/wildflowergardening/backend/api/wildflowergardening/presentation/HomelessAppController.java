package org.wildflowergardening.backend.api.wildflowergardening.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wildflowergardening.backend.api.wildflowergardening.application.HomelessAppService;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.UserContextHolder;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.annotation.HomelessAuthorized;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.interceptor.HomelessAuthInterceptor;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.user.HomelessUserContext;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateHomelessRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateHomelessResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateSleepoverRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessMainResponse;
import org.wildflowergardening.backend.api.wildflowergardening.presentation.dto.UpdateLocationRequest;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.CreateOrUpdateLocationTrackingDto;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.CreateSleepoverDto;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;

@RestController
@RequiredArgsConstructor
@Tag(name = "노숙인 앱 API")
public class HomelessAppController {

  private final HomelessAppService homelessAppService;
  private final UserContextHolder userContextHolder;

  @Operation(summary = "노숙인 계정 생성")
  @PostMapping("/api/v1/homeless-app/homeless")
  public ResponseEntity<CreateHomelessResponse> createHomeless(
      @RequestBody @Valid CreateHomelessRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(homelessAppService.createHomeless(request));
  }

  @HomelessAuthorized
  @Operation(summary = "홈 화면 노숙인 기본 정보 조회")
  @Parameters(@Parameter(
      name = HomelessAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "access-token-example"
  ))
  @GetMapping("/api/v1/homeless-app/homeless")
  public ResponseEntity<HomelessMainResponse> getIdNameByDeviceId() {
    HomelessUserContext homelessContext = (HomelessUserContext) userContextHolder.getUserContext();
    return ResponseEntity.ok(
        homelessAppService.getHomelessMainInfo(homelessContext.getUserId(), LocalDate.now())
    );
  }

  @HomelessAuthorized
  @Operation(summary = "외박 신청")
  @Parameters(@Parameter(
      name = HomelessAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "access-token-example"
  ))
  @PostMapping("/api/v1/homeless-app/sleepover")
  public ResponseEntity<Long> applyForSleepover(
      @RequestBody @Valid CreateSleepoverRequest request
  ) {
    HomelessUserContext homelessContext = (HomelessUserContext) userContextHolder.getUserContext();
    Long sleepoverId = homelessAppService.createSleepover(CreateSleepoverDto.builder()
        .homelessId(homelessContext.getHomelessId())
        .creatorType(UserRole.HOMELESS)
        .shelterId(homelessContext.getShelterId())
        .startDate(request.getStartDate())
        .endDate(request.getEndDate())
        .reason(request.getReason())
        .emergencyContact(request.getEmergencyContact())
        .build());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(sleepoverId);
  }

  @HomelessAuthorized
  @Operation(summary = "외박 신청 가능한 날짜 목록 조회")
  @Parameters(@Parameter(
      name = HomelessAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "access-token-example"
  ))
  @GetMapping("/api/v1/homeless-app/available-sleepover-dates")
  public ResponseEntity<List<LocalDate>> getAvailableSleepoverDates() {
    HomelessUserContext homelessContext = (HomelessUserContext) userContextHolder.getUserContext();
    return ResponseEntity.ok(homelessAppService.getAvailableSleepoverDates(
        homelessContext.getHomelessId()));
  }

  @HomelessAuthorized
  @Operation(summary = "위치 상태 update", description = "30분에 한번씩 호출되는 API")
  @Parameters(@Parameter(
      name = HomelessAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "access-token-example"
  ))
  @PostMapping("/api/v1/homeless-app/location")
  public ResponseEntity<Void> updateLocationStatus(
      @RequestBody @Valid UpdateLocationRequest request
  ) {
    HomelessUserContext homelessContext = (HomelessUserContext) userContextHolder.getUserContext();
    homelessAppService.updateLocationStatus(
        CreateOrUpdateLocationTrackingDto.builder()
            .homelessId(homelessContext.getHomelessId())
            .shelterId(homelessContext.getShelterId())
            .locationStatus(request.getLocationStatus())
            .latitude(request.getLatitude())
            .longitude(request.getLongitude())
            .lastTrackedAt(LocalDateTime.now())
            .build()
    );
    return ResponseEntity.ok().build();
  }

  @HomelessAuthorized
  @Operation(
      summary = "금일 외박 신청 상태 여부 조회",
      description = "밤 10시 반 경 외박 신청 앱푸시 발송을 위해 호출되는 API"
  )
  @Parameters(@Parameter(
      name = HomelessAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "access-token-example"
  ))
  @GetMapping("/api/v1/homeless-app/is-sleepover-tonight")
  public ResponseEntity<Boolean> isSleepoverTonight() {
    HomelessUserContext homelessContext = (HomelessUserContext) userContextHolder.getUserContext();
    boolean isSleepoverTonight = homelessAppService.isSleepoverTonight(
        homelessContext.getHomelessId()
    );
    return ResponseEntity.ok(isSleepoverTonight);
  }

  @HomelessAuthorized
  @Operation(summary = "외박 신청 취소")
  @Parameters(@Parameter(
      name = HomelessAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "access-token-example"
  ))
  @DeleteMapping("/api/v1/homeless-app/sleepover/{sleepoverId}")
  public ResponseEntity<Void> deleteSleepover(
      @PathVariable @Parameter(description = "외박신청내역 id", example = "1") Long sleepoverId
  ) {
    HomelessUserContext homelessContext = (HomelessUserContext) userContextHolder.getUserContext();
    homelessAppService.deleteSleepover(homelessContext.getHomelessId(), sleepoverId);
    return ResponseEntity.ok().build();
  }
}
