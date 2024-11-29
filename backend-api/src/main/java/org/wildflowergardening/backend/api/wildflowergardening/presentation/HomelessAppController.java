package org.wildflowergardening.backend.api.wildflowergardening.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateSleepoverRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessAppMainResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessTermsResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessTokenRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessTokenResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.UpdateLocationRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.request.EmergencyRequest;
import org.wildflowergardening.backend.api.wildflowergardening.presentation.dto.resonse.LocationStatusResponse;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.BaseResponseBody;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.CreateSleepoverDto;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;

@RestController
@RequiredArgsConstructor
@Tag(name = "노숙인 앱 API")
@Validated
public class HomelessAppController {

    private final HomelessAppService homelessAppService;
    private final UserContextHolder userContextHolder;

    @Operation(summary = "노숙인 약관 목록 조회")
    @GetMapping("/api/v1/homeless-app/terms")
    public ResponseEntity<List<HomelessTermsResponse>> getAllTerms() {
        return ResponseEntity.ok(homelessAppService.getAllTerms());
    }

    @Operation(summary = "노숙인 계정 생성")
    @PostMapping("/api/v1/homeless-app/homeless")
    public ResponseEntity<HomelessTokenResponse> createHomeless(
            @RequestBody @Valid CreateHomelessRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(homelessAppService.createHomeless(request));
    }

    @Operation(summary = "기존 계정 토큰 (재)획득", description = "센터 id, 노숙인 이름, 방번호가 일치하면"
            + "토큰 재획득 가능")
    @PostMapping("/api/v1/homeless-app/token-get")
    public ResponseEntity<HomelessTokenResponse> getHomelessToken(
            @RequestBody @Valid HomelessTokenRequest request
    ) {
        return ResponseEntity.ok(homelessAppService.getToken(request));
    }

    @HomelessAuthorized
    @Operation(summary = "홈 화면 내 정보 조회")
    @Parameters(@Parameter(
            name = HomelessAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "access-token-example"
    ))
    @GetMapping("/api/v1/homeless-app/homeless")
    public ResponseEntity<HomelessAppMainResponse> getHomelessMainInfo() {
        HomelessUserContext homelessContext = (HomelessUserContext) userContextHolder.getUserContext();
        return ResponseEntity.ok(homelessAppService.getHomelessMainInfo(homelessContext.getUserId()));
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
    @Operation(summary = "내 외박 신청 목록 조회",
            description = "외박신청 최대일수 및 기간중복 제한으로 인해 한사람 당 조회 데이터가 많지 않을것으로 예상되어 "
                    + "페이지네이션을 넣지 않았습니다.")
    @Parameters(@Parameter(
            name = HomelessAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "access-token-example"
    ))
    @GetMapping("/api/v1/homeless-app/sleepovers")
    public ResponseEntity<List<?>> getSleepovers() {
        HomelessUserContext homelessContext = (HomelessUserContext) userContextHolder.getUserContext();
        return ResponseEntity.ok(
                homelessAppService.getSleepoversEndDateAfterToday(homelessContext.getHomelessId())
        );
    }

    @HomelessAuthorized
    @Operation(summary = "위치 상태 update", description = "30분에 한번씩 호출되는 API")
    @Parameters(@Parameter(
            name = HomelessAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "access-token-example"
    ))
    @PostMapping("/api/v1/homeless-app/location")
    public ResponseEntity<Long> updateLocationStatus(
            @RequestBody @Valid UpdateLocationRequest request
    ) {
        HomelessUserContext homelessContext = (HomelessUserContext) userContextHolder.getUserContext();
        Long locationTrackingId = homelessAppService.createOrUpdateLocationTracking(
                homelessContext.getHomelessId(), homelessContext.getShelterId(), request
        );
        return ResponseEntity.ok(locationTrackingId);
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
        homelessAppService.deleteSleepover(homelessContext.getHomelessId(), sleepoverId, homelessContext.getShelterId());
        return ResponseEntity.ok().build();
    }

    @HomelessAuthorized
    @Operation(summary = "긴급 상황 발생", description = "긴급 상황 발생 시 위치 정보를 담아 저장할 API")
    @Parameters(@Parameter(
            name = HomelessAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "access-token-example"
    ))
    @PostMapping("/api/v1/homeless-app/emergency")
    public ResponseEntity<Void> emergency(
            @RequestBody @Valid EmergencyRequest request
    ) {
        HomelessUserContext homelessContext = (HomelessUserContext) userContextHolder.getUserContext();
        homelessAppService.saveEmergencyLog(homelessContext.getHomelessId(), homelessContext.getShelterId(), request);
        return ResponseEntity.ok().build();
    }

    @HomelessAuthorized
    @Operation(summary = "위치 상태 조회", description = "재실 중이면 IN_SHELTER, 외출 중이면 OUT_SHELTER 반환")
    @Parameters(@Parameter(
            name = HomelessAuthInterceptor.AUTH_HEADER_NAME,
            in = ParameterIn.HEADER,
            example = "access-token-example"
    ))
    @GetMapping("/api/v1/homeless-app/location")
    public ResponseEntity<LocationStatusResponse> getLocationStatus() {
        HomelessUserContext homelessContext = (HomelessUserContext) userContextHolder.getUserContext();
        String result = homelessAppService.getStatusLocationByHomelessId(homelessContext.getHomelessId(), homelessContext.getShelterId());

        return ResponseEntity.ok(LocationStatusResponse.builder().locationStatus(result).build());
    }

}
