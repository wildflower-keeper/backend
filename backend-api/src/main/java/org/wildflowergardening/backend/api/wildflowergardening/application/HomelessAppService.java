package org.wildflowergardening.backend.api.wildflowergardening.application;

import static org.wildflowergardening.backend.core.kernel.application.exception.WildflowerExceptionType.HOMELESS_APP_CREATE_ACCOUNT_SHELTER_ID_PIN_INVALID;
import static org.wildflowergardening.backend.core.kernel.application.exception.WildflowerExceptionType.HOMELESS_APP_ESSENTIAL_TERMS_NOT_AGREED;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.api.kernel.application.exception.ForbiddenException;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.HomelessAppJwtProvider;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.user.HomelessUserContext;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.CreateHomelessRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessAppMainResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessAppMainResponse.UpcomingSleepover;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessAppMainResponse.UpcomingSleepover.UpcomingSleepoverStatus;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessSleepoverResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessTermsResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessTokenRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessTokenResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.UpdateLocationRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.request.EmergencyRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.response.NoticeResponse;
import org.wildflowergardening.backend.api.wildflowergardening.util.PhoneNumberFormatter;
import org.wildflowergardening.backend.core.kernel.application.exception.ApplicationLogicException;
import org.wildflowergardening.backend.core.wildflowergardening.application.*;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.CreateSleepoverDto;
import org.wildflowergardening.backend.core.wildflowergardening.domain.*;

@Service
@RequiredArgsConstructor
public class HomelessAppService {

    private final ShelterPinService shelterPinService;
    private final HomelessTermsService homelessTermsService;
    private final HomelessTermsAgreeService homelessTermsAgreeService;
    private final ShelterService shelterService;
    private final HomelessCommandService homelessCommandService;
    private final HomelessQueryService homelessQueryService;
    private final SleepoverService sleepoverService;
    private final HomelessAppJwtProvider homelessAppJwtProvider;
    private final LocationTrackingService locationTrackingService;
    private final EmergencyService emergencyService;
    private final DailyHomelessCountsService dailyHomelessCountsService;
    private final DailyOutingCountsService dailyOutingCountsService;
    private final DailySleepoverCountsService dailySleepoverCountsService;
    private final DailyEmergencyCountsService dailyEmergencyCountsService;
    private final NoticeRecipientService noticeRecipientService;
    private final NoticeService noticeService;

    public List<HomelessTermsResponse> getAllTerms() {
        return homelessTermsService.findAll(LocalDate.now()).stream()
                .map(homelessTerms -> HomelessTermsResponse.builder()
                        .id(homelessTerms.getId())
                        .title(homelessTerms.getTitle())
                        .detail(homelessTerms.getDetail())
                        .isEssential(homelessTerms.getIsEssential())
                        .build())
                .toList();
    }

    @Transactional(rollbackFor = {Exception.class})
    public HomelessTokenResponse createHomeless(CreateHomelessRequest request) {
        Optional<Shelter> shelterOptional = shelterService.getShelterById(request.getShelterId());

        if (shelterOptional.isEmpty() ||
                !shelterPinService.matches(shelterOptional.get().getId(), request.getShelterPin())
        ) {
            throw new ApplicationLogicException(HOMELESS_APP_CREATE_ACCOUNT_SHELTER_ID_PIN_INVALID);
        }
        Map<Long, Boolean> termsIdAndEssential = homelessTermsService.findAllIdEssential(
                LocalDate.now()
        );
        Set<Long> allTermsId = termsIdAndEssential.keySet();
        List<Long> essentialTermsIds = allTermsId.stream()
                .filter(termsId -> termsIdAndEssential.get(termsId).equals(Boolean.TRUE))
                .toList();
        HashSet<Long> termsIdsToAgree = request.getTermsIdsToAgree();

        // 약관동의 요청 validation
        for (Long termsIdToAgree : termsIdsToAgree) {
            if (!allTermsId.contains(termsIdToAgree)) {
                throw new IllegalArgumentException("존재하지 않는 약관에 동의하셨습니다.");
            }
        }

        // 필수 약관에 모두 동의했는지 검사
        for (Long essentialId : essentialTermsIds) {
            if (!termsIdsToAgree.contains(essentialId)) {
                throw new ApplicationLogicException(HOMELESS_APP_ESSENTIAL_TERMS_NOT_AGREED);
            }
        }

        // 계정 생성
        Long homelessId = homelessCommandService.create(Homeless.builder()
                .name(request.getName())
                .shelter(shelterOptional.get())
                .deviceId(request.getDeviceId())
                .room(request.getRoom())
                .birthDate(request.getBirthDate())
                .phoneNumber(PhoneNumberFormatter.format(request.getPhoneNumber()))
                .admissionDate(request.getAdmissionDate())
                .build());

        // 약관 동의 내역 생성
        homelessTermsAgreeService.createTermsAgrees(homelessId, termsIdsToAgree);

        String token = homelessAppJwtProvider.createToken(HomelessUserContext.builder()
                .homelessId(homelessId)
                .homelessName(request.getName())
                .shelterId(request.getShelterId())
                .build());

        //기본 재실 상태값 생성
        locationTrackingService.createOrUpdate(homelessId, request.getShelterId(), InOutStatus.IN_SHELTER);

        LocalDate targetDate = LocalDate.now();
        DailyHomelessCounts counts = dailyHomelessCountsService.getOrCreateDailyHomelessCount(request.getShelterId(), targetDate);
        counts.setCount(homelessQueryService.count(request.getShelterId()));

        return HomelessTokenResponse.builder()
                .homelessId(homelessId)
                .accessToken(token)
                .build();
    }

    @Transactional
    public HomelessTokenResponse getToken(HomelessTokenRequest request) {

        if (!shelterPinService.matches(request.getShelterId(), request.getShelterPin())) {
            throw new ForbiddenException("핀 번호가 일치하지 않습니다.");
        }

        Homeless homeless = homelessQueryService.getHomelessByNameShelterIdRoom(request.getHomelessName(), request.getShelterId(), request.getRoom())
                .orElseThrow(() -> new ForbiddenException("이름, 센터, 방번호를 확인해주세요."));

        String token = homelessAppJwtProvider.createToken(HomelessUserContext.builder()
                .homelessId(homeless.getId())
                .homelessName(homeless.getName())
                .shelterId(homeless.getShelter().getId())
                .build());

        return HomelessTokenResponse.builder()
                .homelessId(homeless.getId())
                .accessToken(token)
                .build();
    }

    public HomelessAppMainResponse getHomelessMainInfo(Long homelessId) {
        Homeless homeless = homelessQueryService.getOneById(homelessId)
                .orElseThrow(() -> new IllegalArgumentException("노숙인 정보가 존재하지 않습니다."));

        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalDate nowDate = nowDateTime.toLocalTime().isBefore(LocalTime.of(6, 0))
                ? nowDateTime.toLocalDate().minusDays(1)
                : nowDateTime.toLocalDate();

        Optional<UpcomingSleepover> upcomingSleepoverOptional =
                sleepoverService.getUpcomingSleepover(homelessId, nowDate).stream()
                        .map(sleepover -> {
                            UpcomingSleepoverStatus status;

                            if (sleepover.getStartDate().isBefore(nowDate)
                                    || sleepover.getStartDate().isEqual(nowDate)) {
                                if (nowDateTime.toLocalTime().isBefore(LocalTime.of(6, 0))
                                        || nowDateTime.toLocalTime().isAfter(LocalTime.of(23, 0))) {
                                    status = UpcomingSleepoverStatus.IN_PROGRESS;
                                } else {
                                    status = UpcomingSleepoverStatus.TODAY_SCHEDULED;
                                }
                            } else {
                                status = UpcomingSleepoverStatus.FUTURE_SCHEDULED;
                            }
                            return UpcomingSleepover.builder()
                                    .sleepoverId(sleepover.getId())
                                    .nightCount(Period.between(sleepover.getStartDate(), sleepover.getEndDate())
                                            .getDays())
                                    .startDate(sleepover.getStartDate())
                                    .endDate(sleepover.getEndDate())
                                    .status(status)
                                    .build();
                        })
                        .findAny();

        return HomelessAppMainResponse.builder()
                .id(homeless.getId())
                .homelessName(homeless.getName())
                .shelterId(homeless.getShelter().getId())
                .shelterPhone(homeless.getShelter().getPhoneNumber())
                .shelterName(homeless.getShelter().getName())
                .upcomingSleepover(upcomingSleepoverOptional.orElse(null))
                .build();
    }

    @Transactional
    public Long createSleepover(CreateSleepoverDto dto) {
        if (!dto.getEndDate().isAfter(dto.getStartDate())) {
            throw new IllegalArgumentException("외박신청 종료일은 시작일의 다음날 이후여야 합니다.");
        }
        // 외박 기간 허용 범위 검사
        // Sleepover.calcMinStartDate(now) 이상, Sleepover.calcMaxEndDate(now) 이하 기간동안 외박 신청 가능
        LocalDate now = LocalDate.now();
        if (dto.getStartDate().isBefore(Sleepover.calcMinStartDate(now))
                || dto.getEndDate().isAfter(Sleepover.calcMaxEndDate(now))) {
            throw new IllegalArgumentException("외박 신청 가능한 일자 범위를 벗어났습니다.");
        }


        LocalDate currentDate = dto.getStartDate();
        while (!currentDate.isAfter(dto.getEndDate())) {
            DailySleepoverCounts dailySleepoverCounts = dailySleepoverCountsService.getOrCreateDailySleepoverCounts(dto.getShelterId(), currentDate);
            dailySleepoverCounts.setCount(dailySleepoverCounts.getCount() + 1);

            currentDate = currentDate.plusDays(1);
        }


        return sleepoverService.create(dto);
    }

    public List<LocalDate> getAvailableSleepoverDates(Long homelessId) {
        LocalDate now = LocalDate.now();

        LocalDate periodStart = Sleepover.calcMinStartDate(now).plusDays(1);
        LocalDate periodEnd = Sleepover.calcMaxEndDate(now).minusDays(1);

        Set<LocalDate> exclusiveDates = new HashSet<>();

        List<Sleepover> alreadyExistSleepovers = sleepoverService.getSleepoversForPeriod(
                homelessId, periodStart, periodEnd
        );
        for (Sleepover sleepover : alreadyExistSleepovers) {
            for (LocalDate date = sleepover.getStartDate()
                 ; !date.isAfter(sleepover.getEndDate())
                    ; date = date.plusDays(1)) {
                exclusiveDates.add(date);
            }
        }
        List<LocalDate> resultDates = new ArrayList<>();

        for (LocalDate date = periodStart; !date.isAfter(periodEnd); date = date.plusDays(1)) {
            if (exclusiveDates.contains(date)) {
                continue;
            }
            resultDates.add(date);
        }
        return resultDates;
    }

    public List<HomelessSleepoverResponse> getSleepoversEndDateAfterToday(Long homelessId) {
        LocalDate now = LocalDate.now();
        return sleepoverService.getAllSleepoversEndDateAfter(homelessId, LocalDate.now()).stream()
                .map(sleepover -> HomelessSleepoverResponse.builder()
                        .sleepoverId(sleepover.getId())
                        .startDate(sleepover.getStartDate())
                        .endDate(sleepover.getEndDate())
                        .reason(sleepover.getReason())
                        .isCancelable(sleepover.cancelableAt(now))
                        .build())
                .toList();
    }

    @Transactional
    public Long createOrUpdateLocationTracking(
            Long homelessId, Long shelterId, UpdateLocationRequest request
    ) {
        Long result = locationTrackingService.createOrUpdate(
                homelessId, shelterId, request.getLocationStatus());

        if (request.getLocationStatus() == InOutStatus.OUT_SHELTER) {
            LocalDate targetDate = LocalDate.now();
            DailyOutingCounts dailyOutingCounts = dailyOutingCountsService.getOrCreateDailyOutingCounts(shelterId, targetDate);
            dailyOutingCounts.setCount(dailyOutingCounts.getCount() + 1);
        }

        return result;
    }

    public boolean isSleepoverTonight(Long homelessId) {
        return !sleepoverService.filterSleepoverHomelessIds(List.of(homelessId), LocalDate.now())
                .isEmpty();
    }

    @Transactional
    public void deleteSleepover(Long homelessId, Long sleepoverId, Long shelterId) {
        Sleepover sleepover = sleepoverService.delete(homelessId, sleepoverId);

        LocalDate currentDate = sleepover.getStartDate();
        while (!currentDate.isAfter(sleepover.getEndDate())) {
            DailySleepoverCounts dailySleepoverCounts = dailySleepoverCountsService.getOrCreateDailySleepoverCounts(shelterId, currentDate);
            dailySleepoverCounts.setCount(dailySleepoverCounts.getCount() - 1);

            currentDate = currentDate.plusDays(1);
        }
    }

    @Transactional
    public void saveEmergencyLog(Long homelessId, Long shelterId, EmergencyRequest request) {

        Homeless homeless = homelessQueryService.getOneById(homelessId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        LocalDate targetDate = LocalDate.now();
        DailyEmergencyCounts dailyEmergencyCounts = dailyEmergencyCountsService.getOrCreateDailyEmergencyCounts(shelterId, targetDate);

        dailyEmergencyCounts.setCount(dailyEmergencyCounts.getCount() + 1);

        emergencyService.save(EmergencyLog.builder()
                .homless(homeless)
                .shelterId(shelterId)
                .latitude(request.getLocation().getLat())
                .longitude(request.getLocation().getLng())
                .build());

    }

    public String getStatusLocationByHomelessId(Long homelessId, Long shelterId) {
        LocationTracking locationTracking = locationTrackingService.getLocationByHomelessId(homelessId, shelterId);

        InOutStatus status = locationTracking.getInOutStatus();
        if (status == InOutStatus.IN_SHELTER) {
            return "IN_SHELTER";
        } else {
            return "OUT_SHELTER";
        }
    }

    @Transactional
    public void updateNoticeReadStatus(Long homelessId, Long noticeId, boolean status) {
        noticeRecipientService.updateReadStatus(noticeId, homelessId, status);
    }

    public List<NoticeResponse> getNoticeListByHomeless(Long homelessId) {
        List<Long> noticeIdList = noticeRecipientService.getNoticeIdsByHomelessId(homelessId);
        List<NoticeResponse> noticeList = new ArrayList<>();
        for (Long id : noticeIdList) {
            Notice notice = noticeService.getNoticeById(id).orElseThrow(() -> new IllegalArgumentException("id= " + id + "인 공지사항이 없습니다."));

            noticeList.add(NoticeResponse.builder()
                    .id(notice.getId())
                    .title(notice.getTitle())
                    .contents(notice.getContents())
                    .sendAt(notice.getCreatedAt()).build());

        }
        return noticeList;
    }
}
