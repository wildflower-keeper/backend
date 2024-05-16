package org.wildflowergardening.backend.api.wildflowergardening.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.wildflowergardening.backend.api.wildflowergardening.application.HomelessAppService;
import org.wildflowergardening.backend.api.wildflowergardening.application.HomelessAuthorized;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateHomelessRequest;
import org.wildflowergardening.backend.api.wildflowergardening.presentation.dto.HomelessIdNameResponse;
import org.wildflowergardening.backend.core.wildflowergardening.application.UserContextHolder;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.UserContext;

@RestController
@RequiredArgsConstructor
@Tag(name = "노숙인 스마트폰 앱에서 호출하는 API")
public class HomelessAppController {

  private final HomelessAppService homelessAppService;
  private final UserContextHolder userContextHolder;

  @Operation(summary = "노숙인 계정 생성")
  @PostMapping("/api/v1/homeless")
  public ResponseEntity<Long> createHomeless(
      @RequestBody @Valid CreateHomelessRequest request
  ) {
    Long homelessId = homelessAppService.createHomeless(request);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(homelessId);
  }

  @HomelessAuthorized
  @Operation(summary = "노숙인 정보 조회 by 디바이스 id")
  @GetMapping("/api/v1/homeless")
  public ResponseEntity<HomelessIdNameResponse> getIdNameByDeviceId(
      @RequestHeader(value = "device-id", required = false) String deviceId
  ) {
    UserContext userContext = userContextHolder.getUserContext();
    return ResponseEntity.ok(HomelessIdNameResponse.builder()
        .id(userContext.getUserId())
        .name(userContext.getUsername())
        .build());
  }
}
