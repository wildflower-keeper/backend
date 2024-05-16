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
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateShelterDto;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.SessionDto;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.ShelterLoginDto;
import org.wildflowergardening.backend.core.wildflowergardening.application.SessionContextHolder;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.SessionContext;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.ShelterIdNameDto;

@RestController
@RequiredArgsConstructor
@Tag(name = "1. 센터")
public class ShelterManagingController {

  private final ShelterManagingService shelterManagingService;
  private final SessionContextHolder sessionContextHolder;

  @PostMapping("/admin/v1/shelter")
  @Operation(summary = "센터 생성")
  public ResponseEntity<Long> createShelter(
      @RequestHeader(value = "ADMIN_AUTH") String adminAuth,
      @RequestBody @Valid CreateShelterDto request
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
  public ResponseEntity<SessionDto> login(
      @RequestBody ShelterLoginDto shelterLoginDto
  ) {
    return ResponseEntity.ok(shelterManagingService.login(shelterLoginDto));
  }

  @Deprecated
  @GetMapping("/api/v1/shelter/interceptor-test")
  @ShelterAuthorized
  @Operation(summary = "센터 authorization test")
  public ResponseEntity<ShelterIdNameDto> interceptorTest(
      @RequestHeader(value = "session-id", required = false) String sessionId
  ) {
    SessionContext sessionContext = sessionContextHolder.getSessionContext();
    return ResponseEntity.ok(ShelterIdNameDto.builder()
        .shelterId(sessionContext.getSessionId())
        .shelterName(sessionContext.getUsername())
        .build());
  }
}
