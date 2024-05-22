package org.wildflowergardening.backend.api.wildflowergardening.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wildflowergardening.backend.api.wildflowergardening.application.ShelterAdminAppService;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.ShelterAdminAuthInterceptor;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.ShelterAuthorized;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.SessionResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.ShelterLoginRequest;
import org.wildflowergardening.backend.core.wildflowergardening.application.UserContextHolder;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.ShelterIdNameDto;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.ShelterUserContext;

@RestController
@RequiredArgsConstructor
@Tag(name = "센터 admin API")
public class ShelterAdminAppController {

  private final ShelterAdminAppService shelterAdminAppService;
  private final UserContextHolder userContextHolder;

  @PostMapping("/api/v1/shelter-admin/login")
  @Operation(summary = "센터 관리자 로그인")
  public ResponseEntity<SessionResponse> login(
      @RequestBody @Valid ShelterLoginRequest shelterLoginRequest
  ) {
    return ResponseEntity.ok(shelterAdminAppService.login(shelterLoginRequest));
  }

  @ShelterAuthorized
  @Parameters(@Parameter(
      name = ShelterAdminAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "session-id-example"
  ))
  @Deprecated
  @GetMapping("/api/v1/shelter-admin/interceptor-test")
  @Operation(summary = "센터 authorization test")
  public ResponseEntity<ShelterIdNameDto> interceptorTest() {
    ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();

    return ResponseEntity.ok(ShelterIdNameDto.builder()
        .shelterId(shelterContext.getShelterId())
        .shelterName(shelterContext.getShelterName())
        .build());
  }

  @ShelterAuthorized
  @Parameters(@Parameter(
      name = ShelterAdminAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "session-id-example"
  ))
  @GetMapping("/api/v1/shelter-admin/homeless-people")
  @Operation(summary = "노숙인 목록 조회")
  public ResponseEntity<NumberPageResult<HomelessResponse>> getHomelessPage(
      @RequestParam(defaultValue = "1") int pageNumber,
      @RequestParam(defaultValue = "20") int pageSize
  ) {
    ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
    LocalDate now = LocalDate.now();
    return ResponseEntity.ok(shelterAdminAppService.getHomelessPage(
        shelterContext.getShelterId(), pageNumber, pageSize
    ));
  }
}
