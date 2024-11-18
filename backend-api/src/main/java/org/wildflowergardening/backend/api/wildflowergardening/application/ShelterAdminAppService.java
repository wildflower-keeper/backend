package org.wildflowergardening.backend.api.wildflowergardening.application;

import static org.wildflowergardening.backend.core.kernel.application.exception.WildflowerExceptionType.SHELTER_ADMIN_LOGIN_CODE_INVALID;
import static org.wildflowergardening.backend.core.kernel.application.exception.WildflowerExceptionType.SHELTER_ADMIN_LOGIN_ID_PASSWORD_INVALID;

import io.micrometer.common.util.StringUtils;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.api.kernel.application.exception.ForbiddenException;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.*;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessCountResponse.EmergencyCount;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessCountResponse.LocationTrackingCount;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessCountResponse.SleepoverCount;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse.PageInfoResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.request.ShelterAccountRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.response.EmergencyLogItem;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.response.EmergencyResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.response.HomelessDetailResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.pager.HomelessFilterPagerProvider;
import org.wildflowergardening.backend.api.wildflowergardening.presentation.dto.request.VerificationCodeRequest;
import org.wildflowergardening.backend.api.wildflowergardening.util.PhoneNumberFormatter;
import org.wildflowergardening.backend.core.kernel.application.exception.ApplicationLogicException;
import org.wildflowergardening.backend.core.wildflowergardening.application.*;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.domain.*;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.Session;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;

@Service
@RequiredArgsConstructor
public class ShelterAdminAppService {

    private final SessionService sessionService;
    private final ShelterService shelterService;
    private final PasswordEncoder passwordEncoder;
    private final ShelterPinService shelterPinService;
    private final HomelessFilterPagerProvider homelessFilterPagerProvider;
    private final SleepoverService sleepoverService;
    private final HomelessQueryService homelessQueryService;
    private final LocationTrackingService locationTrackingService;
    private final HomelessCommandService homelessCommandService;
    private final ChiefOfficerService chiefOfficerService;
    private final DutyOfficerService dutyOfficerService;
    private final EmergencyService emergencyService;
    private final MailService mailService;
    private final DailyHomelessCountsService dailyHomelessCountsService;
    private final DailyOutingCountsService dailyOutingCountsService;
    private final DailyEmergencyCountsService dailyEmergencyCountsService;
    private final DailySleepoverCountsService dailySleepoverCountsService;
    private final ShelterAccountService shelterAccountService;


    public SessionResponse login(ShelterLoginRequest dto) {
        ShelterAccount shelterAccount = shelterAccountService.getShelterAccountByEmail(dto.getEmail());

        if (!passwordEncoder.matches(dto.getPw(), shelterAccount.getPassword())) {
            throw new ApplicationLogicException(SHELTER_ADMIN_LOGIN_ID_PASSWORD_INVALID);
        }

        LocalDateTime now = LocalDateTime.now();

        byte[] randomBytes = new byte[80];
        new SecureRandom().nextBytes(randomBytes);
        Session session = Session.builder()
                .token(Base64.getUrlEncoder().encodeToString(randomBytes).substring(0, 80))
                .userRole(shelterAccount.getUserRole())
                .shelterId(shelterAccount.getShelterId())
                .userId(shelterAccount.getId())
                .username(shelterAccount.getName())
                .createdAt(now)
                .expiredAt(now.plusMinutes(30))
                .build();

        session = sessionService.save(session);

        return SessionResponse.builder()
                .authToken(session.getToken())
                .expiredAt(session.getExpiredAt())
                .build();
    }

    public SessionResponse checkCode(VerificationCodeRequest request) {
        if (!mailService.checkVerificationCode(request.getEmail(), request.getCode())) {
            throw new ApplicationLogicException(SHELTER_ADMIN_LOGIN_CODE_INVALID);
        }
        ShelterAccount shelterAccount = shelterAccountService.getShelterAccountByEmail(request.getEmail());
        LocalDateTime now = LocalDateTime.now();
        byte[] randomBytes = new byte[80];
        new SecureRandom().nextBytes(randomBytes);
        Session session = Session.builder()
                .token(Base64.getUrlEncoder().encodeToString(randomBytes).substring(0, 80))
                .userRole(UserRole.SHELTER)
                .userId(shelterAccount.getId())
                .username(shelterAccount.getName())
                .createdAt(now)
                .expiredAt(now.plusMinutes(30))
                .build();

        session = sessionService.save(session);

        return SessionResponse.builder()
                .authToken(session.getToken())
                .expiredAt(session.getExpiredAt())
                .build();
    }

    public void sendCode(ShelterLoginRequest request) {
        ShelterAccount shelterAccount = shelterAccountService.getShelterAccountByEmail(request.getEmail());

        //비밀번호 맞는지 확인
        if (!passwordEncoder.matches(request.getPw(), shelterAccount.getPassword())) {
            throw new ApplicationLogicException(SHELTER_ADMIN_LOGIN_ID_PASSWORD_INVALID);
        }

        mailService.sendVerificationCodeMail(request.getEmail());
    }

    public ShelterPinResponse getPin(Long shelterId) {
        ShelterPin shelterPin = shelterPinService.getShelterPin(shelterId);
        return ShelterPinResponse.builder()
                .pin(shelterPin.getPin())
                .expiredAt(shelterPin.calcExpiredAt())
                .build();
    }

    public String getShelterInfo(Long shelterId) {
        return shelterService.getShelterById(shelterId).orElseThrow(() -> new IllegalArgumentException("보호소가 존재하지 않습니다.")).getName();
    }

    public void logout(Long userId) {
        sessionService.deleteAllBy(UserRole.SHELTER, userId);
    }

    public List<ChiefOfficerResponse> getChiefOfficers(Long shelterId) {
        return chiefOfficerService.getAll(shelterId).stream()
                .map(chiefOfficer -> ChiefOfficerResponse.builder()
                        .chiefOfficerId(chiefOfficer.getId())
                        .name(chiefOfficer.getName())
                        .phoneNumber(chiefOfficer.getPhoneNumber())
                        .build())
                .toList();
    }

    public NumberPageResponse<HomelessResponse> getHomelessPage(HomelessPageRequest pageRequest) {
        return homelessFilterPagerProvider.from(pageRequest.getFilterType())
                .getPage(pageRequest);
    }

    public HomelessDetailResponse getHomeless(Long shelterId, Long homelessId, LocalDate targetDate) {
        Homeless homeless = homelessQueryService.getOneByIdAndShelter(homelessId, shelterId).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        // TODO : targetdate를 기준으로 외박 신청 여부(true/false)로 반환
        boolean isSleepOverDate = sleepoverService.exist(homelessId, targetDate);

        // TODO : 마지막 위치 상태, 마지막 위치 확인 일시
        LocationTracking locationTracking = locationTrackingService.getLocationByHomelessId(homelessId, shelterId);

        return HomelessDetailResponse.builder()
                .id(homeless.getId())
                .name(homeless.getName())
                .room(homeless.getRoom())
                .birthDate(homeless.getBirthDate())
                .targetDateSleepover(isSleepOverDate)
                .lastLocationStatus(locationTracking.getInOutStatus())
                .lastLocationTrackedAt(locationTracking.getLastUpdatedAt())
                .phoneNumber(homeless.getPhoneNumber())
                .admissionDate(homeless.getAdmissionDate())
                .memo(homeless.getMemo())
                .build();
    }

    public HomelessCountResponse countHomeless(Long shelterId, LocalDateTime targetDateTime) {
        long totalHomelessCount = homelessQueryService.count(shelterId);

        // 외박
        LocalDate sleepoverTargetDate = targetDateTime.toLocalDate();
        long sleepoverCount = sleepoverService.count(shelterId, sleepoverTargetDate);

        // 외출
        Map<Long, LocationTracking> lastLocationMap =        // map key : Homeless id
                locationTrackingService.findAllByShelterId(shelterId);

        //긴급상황
        List<EmergencyLog> emergencyLogList = emergencyService.getEmergencyListOneDay(shelterId, targetDateTime);

        long trackedCount = lastLocationMap.size();
        long inShelterCount = lastLocationMap.values().stream()
                .filter(locationTracking ->
                        locationTracking.getInOutStatus() == InOutStatus.IN_SHELTER)
                .count();
        long outingCount = lastLocationMap.values().stream()
                .filter(locationTracking ->
                        locationTracking.getInOutStatus() == InOutStatus.OUT_SHELTER)
                .count();

        return HomelessCountResponse.builder()
                .totalHomelessCount(totalHomelessCount)
                .sleepoverCount(
                        SleepoverCount.builder()
                                .targetDate(sleepoverTargetDate)
                                .count(sleepoverCount)
                                .build()
                )
                .locationTrackingCount(
                        LocationTrackingCount.builder()
                                .locationTrackedHomelessCount(trackedCount)
                                .locationTrackedAfter(targetDateTime)
                                .outingCount(outingCount)
                                .inShelterCount(inShelterCount)
                                .build()
                )
                .emergencyCount(
                        EmergencyCount.builder()
                                .emergencyOccurredAfter(targetDateTime.minusHours(24))
                                .count((long) emergencyLogList.size())
                                .build()
                )
                .build();
    }

    public NumberPageResponse<ShelterAdminSleepoverResponse> getPage(
            Long shelterId, int pageNumber, int pageSize
    ) {
        NumberPageResult<Sleepover> result = sleepoverService.getPage(shelterId, pageNumber, pageSize);

        List<Long> homelessIds = result.getItems().stream()
                .map(Sleepover::getHomelessId)
                .collect(Collectors.toList());

        Map<Long, LocationTracking> locationTrackingMap = locationTrackingService.getAll(homelessIds);

        return NumberPageResponse.<ShelterAdminSleepoverResponse>builder()
                .items(result.getItems().stream()
                        .map(sleepover -> ShelterAdminSleepoverResponse.builder()
                                .sleepoverId(sleepover.getId())
                                .homelessId(sleepover.getHomelessId())
                                .homelessName(sleepover.getHomelessName())
                                .homelessRoom(sleepover.getHomelessRoom())
                                .homelessPhoneNumber(sleepover.getHomelessPhoneNumber())
                                .status(Optional.ofNullable(locationTrackingMap.get(sleepover.getHomelessId()))
                                        .map(LocationTracking::getInOutStatus)
                                        .orElse(InOutStatus.IN_SHELTER))
                                .reason(sleepover.getReason())
                                .startDate(sleepover.getStartDate())
                                .endDate(sleepover.getEndDate())
                                .createdAt(sleepover.getCreatedAt())
                                .build())
                        .collect(Collectors.toList()))
                .pagination(PageInfoResponse.of(result.getPagination()))
                .build();
    }

    @Transactional
    public void updateHomelessInfo(
            Long shelterId, Long homelessId, UpdateHomelessRequest request
    ) {
        Homeless homeless = homelessQueryService.getOneById(homelessId)
                .orElseThrow(() -> new IllegalArgumentException("not exist homeless"));

        if (!homeless.getShelter().getId().equals(shelterId)) {
            throw new ForbiddenException("");
        }
        homeless.setName(
                StringUtils.isBlank(request.getName())
                        ? homeless.getName()
                        : request.getName()
        );
        homeless.setRoom(
                StringUtils.isBlank(request.getRoom())
                        ? homeless.getRoom()
                        : request.getRoom()
        );
        homeless.setPhoneNumber(
                StringUtils.isBlank(request.getPhoneNumber())
                        ? homeless.getPhoneNumber()
                        : PhoneNumberFormatter.format(request.getPhoneNumber())
        );
        homeless.setBirthDate(
                request.getBirthDate() == null
                        ? homeless.getBirthDate()
                        : request.getBirthDate()
        );
        homeless.setAdmissionDate(
                request.getAdmissionDate() == null
                        ? homeless.getAdmissionDate()
                        : request.getAdmissionDate()
        );
        homeless.setMemo(
                request.getMemo() == null
                        ? homeless.getMemo()
                        : request.getMemo()
        );
    }

    @Transactional
    public void deleteHomeless(Long homelessId, Long shelterId) {
        LocalDate targetDate = LocalDate.now();
        homelessCommandService.deleteHomeless(homelessId, shelterId);
        locationTrackingService.deleteInoutStatus(homelessId, shelterId);
        DailyHomelessCounts counts = dailyHomelessCountsService.getOrCreateDailyHomelessCount(shelterId, targetDate);
        counts.setCount(homelessQueryService.count(shelterId));
    }

    public Long createChiefOfficer(Long shelterId, String name, String phoneNumber) {
        return chiefOfficerService.create(shelterId, name, phoneNumber);
    }

    public void updateChiefOfficer(
            Long shelterId, Long chiefOfficerId, String name, String phoneNumber
    ) {
        chiefOfficerService.update(shelterId, chiefOfficerId, name, phoneNumber);
    }

    public void deleteChiefOfficer(Long shelterId, Long chiefOfficerId) {
        chiefOfficerService.delete(shelterId, chiefOfficerId);
    }

    public void createDutyOfficers(Long shelterId, List<DutyOfficerCreateRequest> requests) {
        dutyOfficerService.create(
                requests.stream()
                        .map(request -> DutyOfficer.builder()
                                .shelterId(shelterId)
                                .name(request.getName())
                                .phoneNumber(PhoneNumberFormatter.format(request.getPhoneNumber()))
                                .targetDate(request.getTargetDate())
                                .build())
                        .toList()
        );
    }

    public List<DutyOfficerResponse> getDutyOfficers(Long shelterId, LocalDate startDate,
                                                     LocalDate endDate) {
        return dutyOfficerService.getList(shelterId, startDate, endDate).stream()
                .map(dutyOfficer -> DutyOfficerResponse.builder()
                        .dutyOfficerId(dutyOfficer.getId())
                        .name(dutyOfficer.getName())
                        .phoneNumber(dutyOfficer.getPhoneNumber())
                        .targetDate(dutyOfficer.getTargetDate())
                        .build())
                .toList();
    }

    @Transactional
    public Long createHomeless(Long shelterId, CreateHomelessByAdminRequest request) {
        Shelter shelter = shelterService.getShelterById(shelterId)
                .orElseThrow(() -> new IllegalArgumentException("id=" + shelterId + "인 센터가 존재하지 않습니다."));
        LocalDate targetDate = LocalDate.now();
        long result = homelessCommandService.create(Homeless.builder()
                .name(request.getName())
                .shelter(shelter)
                .deviceId(null)
                .room(request.getRoom())
                .birthDate(request.getBirthDate())
                .phoneNumber(PhoneNumberFormatter.format(request.getPhoneNumber()))
                .admissionDate(targetDate)
                .memo(request.getMemo())
                .build());

        locationTrackingService.createOrUpdate(result, shelterId, InOutStatus.IN_SHELTER);
        DailyHomelessCounts counts = dailyHomelessCountsService.getOrCreateDailyHomelessCount(shelterId, targetDate);
        counts.setCount(homelessQueryService.count(shelterId));

        return result;
    }

    //전체 위급 상황 발생 내역 조회
    public EmergencyResponse getEmergencyListByShelterId(Long shelterId) {
        List<EmergencyLog> allList = emergencyService.getEmergencyLogByShelterId(shelterId);

        List<EmergencyLogItem> list = new ArrayList<>();
        for (EmergencyLog log : allList) {
            EmergencyLogItem item = EmergencyLogItem.builder()
                    .id(log.getId())
                    .name(log.getHomless().getName())
                    .phNumber(log.getHomless().getPhoneNumber())
                    .date(log.getCreatedAt())
                    .location(Location.builder()
                            .lat(log.getLatitude())
                            .lng(log.getLongitude()).build())
                    .build();
            list.add(item);
        }

        return EmergencyResponse.builder()
                .result(String.valueOf(list.size()))
                .logs(list)
                .build();
    }

    @Transactional
    public void updateHomelessInOutStatus(Long shelterId, Long homelessId, UpdateLocationRequest request) {
        Homeless homeless = homelessQueryService.getOneByIdAndShelter(homelessId, shelterId)
                .orElseThrow(() -> new IllegalArgumentException("노숙인 정보가 존재하지 않습니다."));

        locationTrackingService.createOrUpdate(homelessId, shelterId, request.getLocationStatus());

        if (request.getLocationStatus() == InOutStatus.OUT_SHELTER) {
            LocalDate targetDate = LocalDate.now();
            DailyOutingCounts dailyOutingCounts = dailyOutingCountsService.getOrCreateDailyOutingCounts(shelterId, targetDate);
            dailyOutingCounts.setCount(dailyOutingCounts.getCount() + 1);
        }
    }

    public List<Long> monthlyHomelessCounts(Long shelterId, LocalDate targetDate) {
        if (targetDate == null) {
            targetDate = LocalDate.now();
        }
        return dailyHomelessCountsService.getMonthlyCounts(shelterId, targetDate);
    }

    public List<Long> monthlyOutingCounts(Long shelterId, LocalDate targetDate) {
        if (targetDate == null) {
            targetDate = LocalDate.now();
        }
        return dailyOutingCountsService.getMonthlyCounts(shelterId, targetDate);
    }

    public List<Long> monthlyEmergencyCounts(Long shelterId, LocalDate targetDate) {
        if (targetDate == null) {
            targetDate = LocalDate.now();
        }

        return dailyEmergencyCountsService.getMonthlyCounts(shelterId, targetDate);
    }

    public List<Long> monthlySleepoverCounts(Long shelterId, LocalDate targetDate) {
        if (targetDate == null) {
            targetDate = LocalDate.now();
        }

        return dailySleepoverCountsService.getMonthlyCounts(shelterId, targetDate);
    }

    public Long createShelterAccount(ShelterAccountRequest request, Long shelterId) {
        ShelterAccount shelterAccount = ShelterAccount.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .userRole(UserRole.SHELTER)
                .shelterId(shelterId)
                .remark(request.getRemark())
                .build();

        return shelterAccountService.save(shelterAccount);

    }

}
