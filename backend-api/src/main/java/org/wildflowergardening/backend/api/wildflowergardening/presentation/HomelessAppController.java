package org.wildflowergardening.backend.api.wildflowergardening.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wildflowergardening.backend.api.wildflowergardening.application.HomelessAppService;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.HomelessAuthInterceptor;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.HomelessAuthorized;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateHomelessRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateSleepoverRequest;
import org.wildflowergardening.backend.api.wildflowergardening.presentation.dto.HomelessIdNameResponse;
import org.wildflowergardening.backend.api.wildflowergardening.presentation.dto.UpdateLocationRequest;
import org.wildflowergardening.backend.core.wildflowergardening.application.UserContextHolder;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.HomelessUserContext;

@RestController
@RequiredArgsConstructor
@Tag(name = "노숙인 앱 API")
public class HomelessAppController {

  private final HomelessAppService homelessAppService;
  private final UserContextHolder userContextHolder;

  @Operation(summary = "노숙인 계정 생성")
  @PostMapping("/api/v1/homeless-app/homeless")
  public ResponseEntity<Long> createHomeless(
      @RequestBody @Valid CreateHomelessRequest request
  ) {
    Long homelessId = homelessAppService.createHomeless(request);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(homelessId);
  }

  @HomelessAuthorized
  @Operation(summary = "노숙인 정보 조회 by 디바이스 id")
  @Parameters(@Parameter(
      name = HomelessAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "test_device_id"
  ))
  @GetMapping("/api/v1/homeless-app/homeless")
  public ResponseEntity<HomelessIdNameResponse> getIdNameByDeviceId() {
    HomelessUserContext homelessContext = (HomelessUserContext) userContextHolder.getUserContext();

    return ResponseEntity.ok(HomelessIdNameResponse.builder()
        .id(homelessContext.getHomelessId())
        .name(homelessContext.getHomelessName())
        .build());
  }

  @HomelessAuthorized
  @Operation(summary = "외박 신청")
  @Parameters(@Parameter(
      name = HomelessAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "test_device_id"
  ))
  @PostMapping("/api/v1/homeless-app/sleepover")
  public ResponseEntity<Long> applyForSleepover(
      @RequestBody @Valid CreateSleepoverRequest request
  ) {
    HomelessUserContext homelessContext = (HomelessUserContext) userContextHolder.getUserContext();

    Long sleepoverId = homelessAppService.createSleepover(homelessContext, request);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(sleepoverId);
  }

  @HomelessAuthorized
  @Operation(summary = "위치 상태 update")
  @Parameters(@Parameter(
      name = HomelessAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "test_device_id"
  ))
  @PostMapping("/api/v1/homeless-app/location")
  public ResponseEntity<Void> updateLocationStatus(
      @RequestBody @Valid UpdateLocationRequest request
  ) {
    HomelessUserContext homelessContext = (HomelessUserContext) userContextHolder.getUserContext();

    homelessAppService.updateLocationStatus(
        homelessContext.getHomelessId(), request.getLocationStatus(), LocalDateTime.now()
    );
    return ResponseEntity.ok().build();
  }
}
