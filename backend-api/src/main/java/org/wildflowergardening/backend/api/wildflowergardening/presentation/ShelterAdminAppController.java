package org.wildflowergardening.backend.api.wildflowergardening.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wildflowergardening.backend.api.wildflowergardening.application.ShelterAdminAppService;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.UserContextHolder;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.annotation.ShelterAuthorized;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.interceptor.ShelterAdminAuthInterceptor;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.user.ShelterUserContext;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.SessionResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.ShelterLoginRequest;

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
      example = "session-token-example"
  ))
  @GetMapping("/api/v1/shelter-admin/homeless-people")
  @Operation(summary = "노숙인 목록 조회")
  public ResponseEntity<NumberPageResponse<HomelessResponse>> getHomelessPage(
      @RequestParam(defaultValue = "1") @Parameter(description = "조회할 페이지 번호 (1부터 시작)", example = "1") int pageNumber,
      @RequestParam(defaultValue = "20") @Parameter(description = "페이지 당 조회할 item 갯수", example = "20") int pageSize
  ) {
    ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
    return ResponseEntity.ok(shelterAdminAppService.getHomelessPage(
        shelterContext.getShelterId(), pageNumber, pageSize
    ));
  }
}
