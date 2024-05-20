package org.wildflowergardening.backend.api.wildflowergardening.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.wildflowergardening.backend.api.wildflowergardening.application.ShelterAdminAppService;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.ShelterAuthorized;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.SessionResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.ShelterLoginRequest;
import org.wildflowergardening.backend.core.wildflowergardening.application.UserContextHolder;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.ShelterIdNameDto;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.ShelterUserContext;

@RestController
@RequiredArgsConstructor
@Tag(name = "3. 센터 admin API")
public class ShelterAdminAppController {

  private final ShelterAdminAppService shelterAdminAppService;
  private final UserContextHolder userContextHolder;

  @PostMapping("/api/v1/shelter/login")
  @Operation(summary = "센터 관리자 로그인")
  public ResponseEntity<SessionResponse> login(
      @RequestBody @Valid ShelterLoginRequest shelterLoginRequest
  ) {
    return ResponseEntity.ok(shelterAdminAppService.login(shelterLoginRequest));
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
