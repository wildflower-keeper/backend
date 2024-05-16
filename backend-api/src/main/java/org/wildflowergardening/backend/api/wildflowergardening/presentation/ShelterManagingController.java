package org.wildflowergardening.backend.api.wildflowergardening.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.wildflowergardening.backend.api.wildflowergardening.application.ShelterAuthorized;
import org.wildflowergardening.backend.api.wildflowergardening.application.ShelterManagingService;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateShelterRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.SessionResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.ShelterLoginRequest;
import org.wildflowergardening.backend.core.wildflowergardening.application.UserContextHolder;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.ShelterIdNameDto;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.ShelterUserContext;

@RestController
@RequiredArgsConstructor
@Tag(name = "센터 계정 관련 API")
public class ShelterManagingController {

  private final ShelterManagingService shelterManagingService;
  private final UserContextHolder userContextHolder;

  @PostMapping("/admin/v1/shelter")
  @Operation(summary = "센터 생성")
  public ResponseEntity<Long> createShelter(
      @RequestHeader(value = "ADMIN_AUTH") String adminAuth,
      @RequestBody @Valid CreateShelterRequest request
  ) {
    Long shelterId = shelterManagingService.createShelter(adminAuth, request);

    return ResponseEntity.ok(shelterId);
  }

  @GetMapping("/api/v1/shelters")
  @Operation(summary = "센터 목록 조회")
  public ResponseEntity<List<ShelterIdNameDto>> getAllIdName() {
    return ResponseEntity.ok(shelterManagingService.getAllIdName());
  }

  @PostMapping("/api/v1/shelter/login")
  @Operation(summary = "센터 관리자 로그인")
  public ResponseEntity<SessionResponse> login(
      @RequestBody @Valid ShelterLoginRequest shelterLoginRequest
  ) {
    return ResponseEntity.ok(shelterManagingService.login(shelterLoginRequest));
  }

  @ShelterAuthorized
  @Deprecated
  @GetMapping("/api/v1/shelter/interceptor-test")
  @Operation(summary = "센터 authorization test")
  public ResponseEntity<ShelterIdNameDto> interceptorTest(
      @RequestHeader(value = "session-id", required = false) String sessionId
  ) {
    ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();

    return ResponseEntity.ok(ShelterIdNameDto.builder()
        .shelterId(shelterContext.getShelterId())
        .shelterName(shelterContext.getShelterName())
        .build());
  }
}
