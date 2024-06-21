package org.wildflowergardening.backend.api.wildflowergardening.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wildflowergardening.backend.api.wildflowergardening.application.ShelterAdminAppService;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.UserContextHolder;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.annotation.ShelterAuthorized;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.interceptor.ShelterAdminAuthInterceptor;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.user.ShelterUserContext;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.ChiefOfficerResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateChiefOfficerRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessCountResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessFilterType;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessPageRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.SessionResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.ShelterAdminSleepoverResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.ShelterLoginRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.ShelterPinResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.UpdateHomelessRequest;
import org.wildflowergardening.backend.api.wildflowergardening.presentation.dto.UpdateChiefOfficerRequest;
import org.wildflowergardening.backend.api.wildflowergardening.util.PhoneNumberFormatter;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverExcelService;

@RestController
@RequiredArgsConstructor
@Tag(name = "센터 admin API")
@Slf4j
public class ShelterAdminAppController {

  private final ShelterAdminAppService shelterAdminAppService;
  private final UserContextHolder userContextHolder;
  private final SleepoverExcelService sleepoverExcelService;

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
  @GetMapping("/api/v1/shelter-admin/pin")
  @Operation(summary = "핀번호 조회")
  public ResponseEntity<ShelterPinResponse> getPin() {
    ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
    return ResponseEntity.ok(shelterAdminAppService.getPin(shelterContext.getShelterId()));
  }

  @ShelterAuthorized
  @Parameters(@Parameter(
      name = ShelterAdminAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "session-token-example"
  ))
  @GetMapping("/api/v1/shelter-admin/homeless-people")
  @Operation(summary = "노숙인 목록 조회", description = "필터유형(필터값) NONE() LOCATION_STATUS(IN_SHELTER,OUTING) SLEEPOVER() NAME(노숙인성함)")
  public ResponseEntity<NumberPageResponse<HomelessResponse>> getHomelessPage(
      @RequestParam(defaultValue = "NONE") @Parameter(description = "필터 유형", example = "NAME") HomelessFilterType filterType,
      @RequestParam(required = false) @Parameter(description = "필터 값", example = "민수") String filterValue,
      @RequestParam(required = false) @Parameter(description = "외박신청 확인 기준일", example = "2024-05-24") LocalDate sleepoverTargetDate,
      @RequestParam(defaultValue = "1") @Parameter(description = "조회할 페이지 번호 (1부터 시작)", example = "1") int pageNumber,
      @RequestParam(defaultValue = "20") @Parameter(description = "페이지 당 조회할 item 갯수", example = "20") int pageSize
  ) {
    if (sleepoverTargetDate == null) {
      sleepoverTargetDate = LocalDate.now();
    }
    ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
    HomelessPageRequest pageRequest = HomelessPageRequest.builder()
        .shelterId(shelterContext.getShelterId())
        .filterType(filterType)
        .filterValue(filterValue)
        .sleepoverTargetDate(sleepoverTargetDate)
        .pageNumber(pageNumber)
        .pageSize(pageSize)
        .build();
    return ResponseEntity.ok(shelterAdminAppService.getHomelessPage(pageRequest));
  }

  @ShelterAuthorized
  @Parameters(@Parameter(
      name = ShelterAdminAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "session-token-example"
  ))
  @GetMapping("/api/v1/shelter-admin/homeless-people/count")
  @Operation(summary = "통계 데이터 조회 (변경예정)")
  public ResponseEntity<HomelessCountResponse> getHomelessCount(
      @RequestParam(required = false) @Parameter(description = "외박신청 및 위치 확인 기준일시", example = "2024-05-24 18:00:00") LocalDateTime targetDateTime
  ) {
    if (targetDateTime == null) {
      targetDateTime = LocalDateTime.now();
    }
    ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
    return ResponseEntity.ok(shelterAdminAppService.countHomeless(
        shelterContext.getShelterId(), targetDateTime
    ));
  }

  @ShelterAuthorized
  @Parameters(@Parameter(
      name = ShelterAdminAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "session-token-example"
  ))
  @PutMapping("/api/v1/shelter-admin/homeless/{homelessId}")
  @Operation(summary = "노숙인 정보 수정", description = "수정하고싶은 프로퍼티만 채워서 보내시면 됩니다.")
  public ResponseEntity<Void> updateHomelessInfo(
      @PathVariable Long homelessId,
      @RequestBody @Valid UpdateHomelessRequest request
  ) {
    ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
    shelterAdminAppService.updateHomelessInfo(shelterContext.getShelterId(), homelessId, request);
    return ResponseEntity.ok().build();
  }

  @ShelterAuthorized
  @Parameters(@Parameter(
      name = ShelterAdminAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "session-token-example"
  ))
  @GetMapping("/api/v1/shelter-admin/sleepovers")
  @Operation(summary = "외박 신청 목록 조회")
  public ResponseEntity<NumberPageResponse<ShelterAdminSleepoverResponse>> getSleepovers(
      @RequestParam(defaultValue = "1") @Parameter(description = "조회할 페이지 번호 (1부터 시작)", example = "1") int pageNumber,
      @RequestParam(defaultValue = "20") @Parameter(description = "페이지 당 조회할 item 갯수", example = "20") int pageSize
  ) {
    ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
    return ResponseEntity.ok(shelterAdminAppService.getPage(
        shelterContext.getShelterId(), pageNumber, pageSize
    ));
  }

  @ShelterAuthorized
  @Parameters(@Parameter(
      name = ShelterAdminAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "session-token-example"
  ))
  @Operation(summary = "외박신청내역 엑셀 다운로드")
  @GetMapping("/api/v1/shelter-admin/sleepover-xlsx")
  public void downloadSleepoverExcel(
      @RequestParam @Parameter(example = "2024-05-01") LocalDate createdAtStart,
      @RequestParam @Parameter(example = "2024-05-31") LocalDate createdAtEnd,
      HttpServletResponse response
  ) {
    try {
      ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();

      response.setHeader(HttpHeaders.CONTENT_TYPE,
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

      ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
          .filename(createdAtStart.format(DateTimeFormatter.ofPattern("yy_MM_dd")) + ".xlsx",
              StandardCharsets.UTF_8)
          .build();
      response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());

      sleepoverExcelService.create(
          shelterContext.getShelterId(),
          createdAtStart.atTime(0, 0),
          createdAtEnd.atTime(LocalTime.MAX.truncatedTo(ChronoUnit.NANOS)),
          response.getOutputStream()
      );

    } catch (IOException e) {
      response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      log.error("엑셀 생성 실패", e);
    }
  }

  @ShelterAuthorized
  @Parameters(@Parameter(
      name = ShelterAdminAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "session-token-example"
  ))
  @Operation(summary = "노숙인 계정 삭제")
  @DeleteMapping("/api/v1/shelter-admin/homeless/{homelessId}")
  public ResponseEntity<Void> deleteHomeless(@PathVariable Long homelessId) {
    ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
    shelterAdminAppService.deleteHomeless(homelessId, shelterContext.getShelterId());
    return ResponseEntity.ok().build();
  }

  @ShelterAuthorized
  @Parameters(@Parameter(
      name = ShelterAdminAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "session-token-example"
  ))
  @Operation(summary = "책임자 정보 생성")
  @PostMapping("/api/v1/shelter-admin/chief-officer")
  public ResponseEntity<Long> createChiefOfficer(
      @Valid @RequestBody CreateChiefOfficerRequest request
  ) {
    ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
    Long chiefOfficerId = shelterAdminAppService.createChiefOfficer(
        shelterContext.getShelterId(), request.getName(),
        PhoneNumberFormatter.format(request.getPhoneNumber())
    );
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(chiefOfficerId);
  }

  @ShelterAuthorized
  @Parameters(@Parameter(
      name = ShelterAdminAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "session-token-example"
  ))
  @Operation(summary = "센터 책임자 목록 조회")
  @GetMapping("/api/v1/shelter-admin/chief-officers")
  public ResponseEntity<List<ChiefOfficerResponse>> getChiefOfficers() {
    ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
    return ResponseEntity.ok(shelterAdminAppService.getAll(shelterContext.getShelterId()));
  }

  @ShelterAuthorized
  @Parameters(@Parameter(
      name = ShelterAdminAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "session-token-example"
  ))
  @Operation(summary = "책임자 정보 수정")
  @PutMapping("/api/v1/shelter-admin/chief-officer/{chiefOfficerId}")
  public ResponseEntity<Void> updateChiefOfficer(
      @PathVariable @Schema(example = "1") Long chiefOfficerId,
      @RequestBody @Valid UpdateChiefOfficerRequest request
  ) {
    ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
    shelterAdminAppService.updateChiefOfficer(
        shelterContext.getShelterId(),
        chiefOfficerId,
        request.getName(), request.getPhoneNumber()
    );
    return ResponseEntity.ok().build();
  }

  @ShelterAuthorized
  @Parameters(@Parameter(
      name = ShelterAdminAuthInterceptor.AUTH_HEADER_NAME,
      in = ParameterIn.HEADER,
      example = "session-token-example"
  ))
  @Operation(summary = "당직자 정보 생성")
  @PostMapping("/api/v1/shelter-admin/duty-officers")
  public ResponseEntity<Void> createDutyOfficers() {
    // Todo 구현
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
