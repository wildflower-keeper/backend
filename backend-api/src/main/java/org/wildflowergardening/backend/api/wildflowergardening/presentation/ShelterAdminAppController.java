package org.wildflowergardening.backend.api.wildflowergardening.presentation;

import static org.wildflowergardening.backend.api.kernel.config.JsonObjectMapperConfig.DATE_TIME_FORMAT;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wildflowergardening.backend.api.wildflowergardening.application.AwsS3Service;
import org.wildflowergardening.backend.api.wildflowergardening.application.ShelterAdminAppService;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.UserContextHolder;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.annotation.ShelterAdminAuthorized;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.annotation.ShelterAuthorized;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.interceptor.ShelterAuthInterceptor;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.user.ShelterUserContext;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.*;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.request.CreateNoticeRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.request.HomelessInfoPageRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.request.NoticePageRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.request.ShelterAccountRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.response.*;
import org.wildflowergardening.backend.api.wildflowergardening.presentation.dto.ShelterInfoResponse;
import org.wildflowergardening.backend.api.wildflowergardening.presentation.dto.UpdateChiefOfficerRequest;
import org.wildflowergardening.backend.api.wildflowergardening.presentation.dto.request.VerificationCodeRequest;
import org.wildflowergardening.backend.api.wildflowergardening.util.PhoneNumberFormatter;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverExcelService;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.BaseResponseBody;
import org.wildflowergardening.backend.core.wildflowergardening.domain.InOutStatus;

@RestController
@RequiredArgsConstructor
@Tag(name = "센터 admin API")
@Slf4j
@Validated
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

    @PostMapping("/api/v1/shelter-admin/verification-code")
    @Operation(summary = "이메일로 인증 번호 전송")
    public ResponseEntity<? extends BaseResponseBody> sendCode(@RequestBody @Valid ShelterLoginRequest request) {
        shelterAdminAppService.sendCode(request);
        return ResponseEntity.ok().body(new BaseResponseBody<>(200, "메일 전송 성공"));
    }

    @PostMapping("/api/v1/shelter-admin/auth-token")
    @Operation(summary = "관리자 토큰 발급")
    public ResponseEntity<SessionResponse> getToken(@RequestBody @Valid VerificationCodeRequest request) {
        SessionResponse response = shelterAdminAppService.checkCode(request);
        return ResponseEntity.ok(response);
    }

    @ShelterAdminAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @PostMapping("/api/v2/shelter-admin/shelter-account")
    @Operation(summary = "센터 관리자(일반) 생성")
    public ResponseEntity<Long> createShelterAccount(
            @RequestBody @Valid ShelterAccountRequest shelterAccountRequest
    ) {
        ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
        return ResponseEntity.ok(shelterAdminAppService.createShelterAccount(shelterAccountRequest, shelterContext.getShelterId()));
    }

    @ShelterAdminAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @DeleteMapping("/api/v2/shelter-admin/shelter-account/{shelterAccountId}")
    @Operation(summary = "센터 관리자 삭제")
    public ResponseEntity<Long> createShelterAccount(
            @PathVariable Long shelterAccountId
    ) {
        ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
        return ResponseEntity.ok(shelterAdminAppService.deleteShelterAccount(shelterContext.getShelterId(), shelterAccountId));
    }

    @ShelterAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
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
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @PostMapping("/api/v1/shelter-admin/logout")
    @Operation(summary = "센터 관리자 로그아웃")
    public ResponseEntity<?> logout() {
        ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
        shelterAdminAppService.logout(shelterContext.getShelterId());
        return ResponseEntity.ok().build();
    }

    @ShelterAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @Operation(summary = "홈 화면 센터 정보 조회")
    @GetMapping("/api/v1/shelter-admin/shelter")
    public ResponseEntity<ShelterInfoResponse> getShelterInfo() {
        ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
        Long shelterId = shelterContext.getShelterId();

        LocalDate now = LocalDate.now();

        return ResponseEntity.ok(
                ShelterInfoResponse.builder()
                        .shelterName(shelterAdminAppService.getShelterInfo(shelterId))
                        .chiefOfficers(shelterAdminAppService.getChiefOfficers(shelterId))
                        .dutyOfficers(shelterAdminAppService.getDutyOfficers(shelterId, now, now))
                        .build()
        );
    }

    @ShelterAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @GetMapping("/api/v1/shelter-admin/homeless-people")
    @Operation(summary = "노숙인 목록 조회", description = "필터유형(필터값) NONE() InOut_STATUS(IN_SHELTER,OUT_SHELTER, OVERNIGHT_STAY, UNCONFIRMED) SLEEPOVER() NAME(노숙인성함)")
    public ResponseEntity<NumberPageResponse<HomelessResponse>> getHomelessPage(
            @RequestParam(defaultValue = "NONE") @Parameter(description = "필터 유형", example = "NAME") HomelessFilterType filterType,
            @RequestParam(required = false) @Parameter(description = "재실 상태 값", example = "IN_SHELTER") InOutStatus status,
            @RequestParam(required = false) @Parameter(description = "필터 값", example = "민수") String filterValue,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) @Parameter(description = "외박 신청 및 재실/외출여부 조회 기준 일시", example = "2024-06-30 18:00:00.000000") LocalDateTime targetDateTime,
            @RequestParam(defaultValue = "1") @Parameter(description = "조회할 페이지 번호 (1부터 시작)", example = "1") int pageNumber,
            @RequestParam(defaultValue = "20") @Parameter(description = "페이지 당 조회할 item 갯수", example = "20") int pageSize
    ) {
        if (targetDateTime == null) {
            targetDateTime = LocalDateTime.now();
        }
        ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
        HomelessPageRequest pageRequest = HomelessPageRequest.builder()
                .shelterId(shelterContext.getShelterId())
                .filterType(filterType)
                .inOutstatus(status)
                .filterValue(filterValue)
                .targetDateTime(targetDateTime)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .build();
        return ResponseEntity.ok(shelterAdminAppService.getHomelessPage(pageRequest));
    }

    @ShelterAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @Operation(summary = "노숙인 계정 조회")
    @GetMapping("/api/v1/shelter-admin/homeless/{homelessId}")
    public ResponseEntity<HomelessDetailResponse> getHomeless(
            @PathVariable Long homelessId,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) @Parameter(description = "외박 신청 및 재실/외출여부 조회 기준 일시", example = "2024-06-30 18:00:00.000000") LocalDate targetDateTime
    ) {
        ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
        return ResponseEntity.ok(shelterAdminAppService.getHomeless(shelterContext.getShelterId(), homelessId, targetDateTime));
    }


    @ShelterAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @GetMapping("/api/v1/shelter-admin/homeless-people/count")
    @Operation(summary = "통계 데이터 조회 ", description = ""
            + "외박신청 : 기준일시에서 기준일의 외박자수<br/>"
            + "위치확인 : 기준일시로부터 한시간 전 이후 데이터<br/>"
            + "긴급상황 : 기준일시로부터 24시간동안의 발생수")
    public ResponseEntity<HomelessCountResponse> getHomelessCount(
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) @Parameter(description = "기준일시", example = "2024-05-24 18:00:00.000000") LocalDateTime targetDateTime
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
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
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
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
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
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
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
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
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
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
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
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
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
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @Operation(summary = "책임자 정보 삭제")
    @DeleteMapping("/api/v1/shelter-admin/chief-officer/{chiefOfficerId}")
    public ResponseEntity<Void> deleteChiefOfficer(
            @PathVariable @Schema(example = "1") Long chiefOfficerId
    ) {
        ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
        shelterAdminAppService.deleteChiefOfficer(shelterContext.getShelterId(), chiefOfficerId);
        return ResponseEntity.ok().build();
    }

    @ShelterAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @Operation(summary = "당직자 정보 생성")
    @PostMapping("/api/v1/shelter-admin/duty-officers")
    public ResponseEntity<Void> createDutyOfficers(
            @RequestBody @Valid @Size(max = 200) List<DutyOfficerCreateRequest> requests
    ) {
        ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
        shelterAdminAppService.createDutyOfficers(shelterContext.getShelterId(), requests);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ShelterAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @Operation(summary = "노숙인 생성")
    @PostMapping("/api/v1/shelter-admin/homeless")
    public ResponseEntity<Long> createHomeless(@RequestBody CreateHomelessByAdminRequest request) {
        ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
        Long homelessId = shelterAdminAppService.createHomeless(shelterContext.getShelterId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(homelessId);
    }

    //TO DO : shelter id를 기준으로 긴급 상황 전체 조회
    @ShelterAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @Operation(summary = "긴급상황 전체 조회")
    @GetMapping("/api/v1/shelter-admin/emergency")
    public ResponseEntity<EmergencyResponse> getEmergencyLog() {
        ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
        EmergencyResponse response = shelterAdminAppService.getEmergencyListByShelterId(shelterContext.getShelterId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ShelterAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @Operation(summary = "노숙인 재실 여부 변경")
    @PutMapping("/api/v1/shelter-admin/{homelessId}/in-out")
    public ResponseEntity<Void> updateInOutStatus(@PathVariable Long homelessId, @RequestBody UpdateLocationRequest inOutStatus) {
        ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
        shelterAdminAppService.updateHomelessInOutStatus(shelterContext.getShelterId(), homelessId, inOutStatus);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ShelterAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @Operation(summary = "한달 동안 유저 증감 조회")
    @GetMapping("/api/v1/shelter-admin/monthly/homeless-counts")
    public ResponseEntity<List<Long>> getMonthlyUserCounts(
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) @Parameter(description = "통계 자료 조회 일시", example = "2024-06-30") LocalDate targetDate
    ) {
        ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
        List<Long> result = shelterAdminAppService.monthlyHomelessCounts(shelterContext.getShelterId(), targetDate);
        return ResponseEntity.ok().body(result);
    }

    @ShelterAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @Operation(summary = "한달 동안 외출 횟수 증감 조회")
    @GetMapping("/api/v1/shelter-admin/monthly/outing-counts")
    public ResponseEntity<List<Long>> getMonthlyOutingCounts(
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) @Parameter(description = "통계 자료 조회 일시", example = "2024-06-30") LocalDate targetDate
    ) {
        ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
        List<Long> result = shelterAdminAppService.monthlyOutingCounts(shelterContext.getShelterId(), targetDate);
        return ResponseEntity.ok().body(result);
    }

    @ShelterAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @Operation(summary = "한달 동안 긴습 상황 발생 횟수 증감 조회")
    @GetMapping("/api/v1/shelter-admin/monthly/emergency-counts")
    public ResponseEntity<List<Long>> getMonthlyEmergencyCounts(
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) @Parameter(description = "통계 자료 조회 일시", example = "2024-06-30") LocalDate targetDate
    ) {
        ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
        List<Long> result = shelterAdminAppService.monthlyEmergencyCounts(shelterContext.getShelterId(), targetDate);
        return ResponseEntity.ok().body(result);
    }

    @ShelterAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @Operation(summary = "한달 동안 외박 신청 횟수 증감 조회")
    @GetMapping("/api/v1/shelter-admin/monthly/sleepover-counts")
    public ResponseEntity<List<Long>> getMonthlySleepoverCounts(
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) @Parameter(description = "통계 자료 조회 일시", example = "2024-06-30") LocalDate targetDate
    ) {
        ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
        List<Long> result = shelterAdminAppService.monthlySleepoverCounts(shelterContext.getShelterId(), targetDate);
        return ResponseEntity.ok().body(result);
    }

    @ShelterAdminAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @Operation(summary = "관리자 계정 조회(슈퍼 관리자만 가능)")
    @GetMapping("/api/v2/shelter-admin/shelter-account")
    public ResponseEntity<List<ShelterAccountResponse>> getShelterAccounts() {
        ShelterUserContext shelterUserContext = (ShelterUserContext) userContextHolder.getUserContext();
        List<ShelterAccountResponse> result = shelterAdminAppService.getShelterAccountAll(shelterUserContext.getShelterId());
        return ResponseEntity.ok().body(result);
    }

    @ShelterAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @Operation(summary = "공지사항 등록")
    @PostMapping("/api/v2/shelter-admin/notice")
    public ResponseEntity<Map<String, Long>> getMonthlySleepoverCounts(
            @RequestBody @Valid CreateNoticeRequest request
    ) {
        ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
        Long result = shelterAdminAppService.createNotice(shelterContext.getShelterId(), shelterContext.getUserId(), request);
        Map<String, Long> response = Map.of("noticeId", result);
        return ResponseEntity.ok().body(response);
    }

    @ShelterAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @GetMapping("/api/v2/shelter-admin/notice")
    @Operation(summary = "공지 사항 목록 조회", description = "공지 사항 전체 조회")
    public ResponseEntity<NumberPageResponse<NoticeResponse>> getNoticePage(
            @RequestParam(defaultValue = "NONE") @Parameter(description = "필터 유형", example = "NAME") NoticeFilterType filterType,
            @RequestParam(required = false) @Parameter(description = "전체 공지인지 유무", example = "true") Boolean isGlobal,
            @RequestParam(defaultValue = "1") @Parameter(description = "조회할 페이지 번호 (1부터 시작)", example = "1") int pageNumber,
            @RequestParam(defaultValue = "20") @Parameter(description = "페이지 당 조회할 item 갯수", example = "20") int pageSize
    ) {
        ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
        NoticePageRequest pageRequest = NoticePageRequest.builder()
                .shelterId(shelterContext.getShelterId())
                .pageNumber(pageNumber)
                .isGlobal(isGlobal)
                .filterType(filterType)
                .pageSize(pageSize)
                .build();


        return ResponseEntity.ok(shelterAdminAppService.getNoticePage(pageRequest));
    }

    @ShelterAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @GetMapping("/api/v2/shelter-admin/notice-recipient/{noticeId}")
    @Operation(summary = "공지사항 수신 목록 조회", description = "noticeId를 가진 공지사항의 수신 여부 목록을 조회함.")
    public ResponseEntity<NoticeRecipientStatusResponse> getNoticeRecipientReadStatus(
            @PathVariable Long noticeId
    ) {
        ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
        NoticeRecipientStatusResponse response = shelterAdminAppService.getNoticeRecipientStatus(noticeId, shelterContext.getShelterId());
        return ResponseEntity.ok(response);
    }

    @ShelterAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @GetMapping("/api/v2/shelter-admin/notice/{noticeId}")
    @Operation(summary = "공지 사항 조회", description = "noticeId를 가진 공지 사항 조회.")
    public ResponseEntity<NoticeItemResponse> getOneNotice(
            @PathVariable Long noticeId
    ) {
        ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
        NoticeItemResponse response = shelterAdminAppService.getOneNotice(noticeId, shelterContext.getShelterId());
        return ResponseEntity.ok(response);
    }

    @ShelterAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @GetMapping("/api/v2/shelter-admin/homeless-info")
    @Operation(summary = "노숙인 목록 조회", description = "노숙인 상태에 따른 정보 조회")
    public ResponseEntity<HomelessInfoPageResponse<Object>> getNoticePage(
            @RequestParam(defaultValue = "-") @Parameter(description = "필터 유형", example = "OUT") HomelessInfoFilterType filterType,
            @RequestParam(defaultValue = "1") @Parameter(description = "조회할 페이지 번호 (1부터 시작)", example = "1") int pageNumber,
            @RequestParam(defaultValue = "10") @Parameter(description = "페이지 당 조회할 item 갯수", example = "10") int pageSize
    ) {
        ShelterUserContext shelterContext = (ShelterUserContext) userContextHolder.getUserContext();
        HomelessInfoPageRequest pageRequest = HomelessInfoPageRequest.builder()
                .filterType(filterType)
                .pageNumber(pageNumber)
                .shelterId(shelterContext.getShelterId())
                .pageSize(pageSize)
                .build();


        return ResponseEntity.ok(shelterAdminAppService.getHomelessInfoPage(pageRequest));
    }
    
    @ShelterAuthorized
    @Parameters(@Parameter(
            name = ShelterAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "session-token-example"
    ))
    @PostMapping(value = "/api/v2/shelter-admin/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "이미지 업로드", description = "\".jpg\", \".jpeg\", \".png\" 확장자만 가능")
    public ResponseEntity<FileUploadResponse> uploadImage(@RequestPart("multipartFile") MultipartFile multipartFile) {
        return ResponseEntity.ok(shelterAdminAppService.uploadFile(multipartFile));
    }
}
