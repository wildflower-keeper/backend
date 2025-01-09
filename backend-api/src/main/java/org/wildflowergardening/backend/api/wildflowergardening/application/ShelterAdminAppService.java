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
import org.springframework.web.multipart.MultipartFile;
import org.wildflowergardening.backend.api.kernel.application.exception.ForbiddenException;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.*;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessCountResponse.EmergencyCount;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessCountResponse.LocationTrackingCount;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessCountResponse.SleepoverCount;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse.PageInfoResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.request.CreateNoticeRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.request.HomelessInfoPageRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.request.NoticePageRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.request.ShelterAccountRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.response.*;
import org.wildflowergardening.backend.api.wildflowergardening.application.pager.HomelessFilterPagerProvider;
import org.wildflowergardening.backend.api.wildflowergardening.application.pager.HomelessInfoFilterPageProvider;
import org.wildflowergardening.backend.api.wildflowergardening.application.pager.NoticeFilterPagerProvider;
import org.wildflowergardening.backend.api.wildflowergardening.presentation.dto.request.VerificationCodeRequest;
import org.wildflowergardening.backend.api.wildflowergardening.util.PhoneNumberFormatter;
import org.wildflowergardening.backend.core.kernel.application.exception.ApplicationLogicException;
import org.wildflowergardening.backend.core.kernel.config.FCMService;
import org.wildflowergardening.backend.core.kernel.config.dto.FcmMultiSendDto;
import org.wildflowergardening.backend.core.kernel.config.dto.FcmSendDto;
import org.wildflowergardening.backend.core.wildflowergardening.application.NoticeRecipientService;
import org.wildflowergardening.backend.core.wildflowergardening.application.*;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.domain.*;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.Session;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;

@Service
@RequiredArgsConstructor
public class ShelterAdminAppService {

    private final AwsS3Service awsS3Service;
    private final SessionService sessionService;
    private final ShelterService shelterService;
    private final PasswordEncoder passwordEncoder;
    private final ShelterPinService shelterPinService;
    private final HomelessFilterPagerProvider homelessFilterPagerProvider;
    private final NoticeFilterPagerProvider noticeFilterPagerProvider;
    private final HomelessInfoFilterPageProvider homelessInfoFilterPageProvider;
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
    private final NoticeService noticeService;
    private final NoticeRecipientService noticeRecipientService;
    private final FCMService fcmService;

    public SessionResponse login(ShelterLoginRequest dto) {
        ShelterAccount shelterAccount = shelterAccountService.getShelterAccountByEmail(dto.getEmail());

        if (!passwordEncoder.matches(dto.getPw(), shelterAccount.getPassword())) {
            throw new ApplicationLogicException(SHELTER_ADMIN_LOGIN_ID_PASSWORD_INVALID);
        }

        LocalDateTime now = LocalDateTime.now();

        byte[] randomBytes = new byte[80];
        new SecureRandom().nextBytes(randomBytes);
        Session session = Session.builder().token(Base64.getUrlEncoder().encodeToString(randomBytes).substring(0, 80)).userRole(shelterAccount.getUserRole()).shelterId(shelterAccount.getShelterId()).userId(shelterAccount.getId()).username(shelterAccount.getName()).createdAt(now).expiredAt(now.plusHours(12)).build();

        session = sessionService.save(session);

        return SessionResponse.builder().authToken(session.getToken()).expiredAt(session.getExpiredAt()).build();
    }

    public SessionResponse checkCode(VerificationCodeRequest request) {
        if (!mailService.checkVerificationCode(request.getEmail(), request.getCode())) {
            throw new ApplicationLogicException(SHELTER_ADMIN_LOGIN_CODE_INVALID);
        }
        ShelterAccount shelterAccount = shelterAccountService.getShelterAccountByEmail(request.getEmail());
        LocalDateTime now = LocalDateTime.now();
        byte[] randomBytes = new byte[80];
        new SecureRandom().nextBytes(randomBytes);
        Session session = Session.builder().token(Base64.getUrlEncoder().encodeToString(randomBytes).substring(0, 80)).userRole(shelterAccount.getUserRole()).userId(shelterAccount.getId()).shelterId(shelterAccount.getShelterId()).username(shelterAccount.getName()).createdAt(now).expiredAt(now.plusHours(12)).build();

        session = sessionService.save(session);

        return SessionResponse.builder().authToken(session.getToken()).expiredAt(session.getExpiredAt()).build();
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
        return ShelterPinResponse.builder().pin(shelterPin.getPin()).expiredAt(shelterPin.calcExpiredAt()).build();
    }

    public String getShelterInfo(Long shelterId) {
        return shelterService.getShelterById(shelterId).orElseThrow(() -> new IllegalArgumentException("보호소가 존재하지 않습니다.")).getName();
    }

    public void logout(Long userId) {
        sessionService.deleteAllBy(UserRole.SHELTER, userId);
    }

    public List<ChiefOfficerResponse> getChiefOfficers(Long shelterId) {
        return chiefOfficerService.getAll(shelterId).stream().map(chiefOfficer -> ChiefOfficerResponse.builder().chiefOfficerId(chiefOfficer.getId()).name(chiefOfficer.getName()).phoneNumber(chiefOfficer.getPhoneNumber()).build()).toList();
    }

    public NumberPageResponse<HomelessResponse> getHomelessPage(HomelessPageRequest pageRequest) {
        return homelessFilterPagerProvider.from(pageRequest.getFilterType()).getPage(pageRequest);
    }

    public HomelessDetailResponse getHomeless(Long shelterId, Long homelessId, LocalDate targetDate) {
        Homeless homeless = homelessQueryService.getOneByIdAndShelter(homelessId, shelterId).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        boolean isSleepOverDate = sleepoverService.isExist(homelessId, targetDate);

        LocationTracking locationTracking = locationTrackingService.getLocationByHomelessId(homelessId, shelterId);

        return HomelessDetailResponse.builder().id(homeless.getId()).name(homeless.getName()).room(homeless.getRoom()).birthDate(homeless.getBirthDate()).targetDateSleepover(isSleepOverDate).lastLocationStatus(locationTracking.getInOutStatus()).lastLocationTrackedAt(locationTracking.getLastUpdatedAt()).phoneNumber(homeless.getPhoneNumber()).admissionDate(homeless.getAdmissionDate()).memo(homeless.getMemo()).build();
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
        long inShelterCount = lastLocationMap.values().stream().filter(locationTracking -> locationTracking.getInOutStatus() == InOutStatus.IN_SHELTER).count();
        long outingCount = lastLocationMap.values().stream().filter(locationTracking -> locationTracking.getInOutStatus() == InOutStatus.OUT_SHELTER).count();

        return HomelessCountResponse.builder().totalHomelessCount(totalHomelessCount).sleepoverCount(SleepoverCount.builder().targetDate(sleepoverTargetDate).count(sleepoverCount).build()).locationTrackingCount(LocationTrackingCount.builder().locationTrackedHomelessCount(trackedCount).locationTrackedAfter(targetDateTime).outingCount(outingCount).inShelterCount(inShelterCount).build()).emergencyCount(EmergencyCount.builder().emergencyOccurredAfter(targetDateTime.minusHours(24)).count((long) emergencyLogList.size()).build()).build();
    }

    public NumberPageResponse<ShelterAdminSleepoverResponse> getPage(Long shelterId, int pageNumber, int pageSize) {
        NumberPageResult<Sleepover> result = sleepoverService.getPage(shelterId, pageNumber, pageSize);

        List<Long> homelessIds = result.getItems().stream().map(Sleepover::getHomelessId).collect(Collectors.toList());

        Map<Long, LocationTracking> locationTrackingMap = locationTrackingService.getAll(homelessIds);

        return NumberPageResponse.<ShelterAdminSleepoverResponse>builder().items(result.getItems().stream().map(sleepover -> ShelterAdminSleepoverResponse.builder().sleepoverId(sleepover.getId()).homelessId(sleepover.getHomelessId()).homelessName(sleepover.getHomelessName()).homelessRoom(sleepover.getHomelessRoom()).homelessPhoneNumber(sleepover.getHomelessPhoneNumber()).status(Optional.ofNullable(locationTrackingMap.get(sleepover.getHomelessId())).map(LocationTracking::getInOutStatus).orElse(InOutStatus.IN_SHELTER)).reason(sleepover.getReason()).startDate(sleepover.getStartDate()).endDate(sleepover.getEndDate()).createdAt(sleepover.getCreatedAt()).emergencyContact(sleepover.getEmergencyContact()).build()).collect(Collectors.toList())).pagination(PageInfoResponse.of(result.getPagination())).build();
    }

    @Transactional
    public void updateHomelessInfo(Long shelterId, Long homelessId, UpdateHomelessRequest request) {
        Homeless homeless = homelessQueryService.getOneById(homelessId).orElseThrow(() -> new IllegalArgumentException("not exist homeless"));

        if (!homeless.getShelter().getId().equals(shelterId)) {
            throw new ForbiddenException("");
        }
        homeless.setName(StringUtils.isBlank(request.getName()) ? homeless.getName() : request.getName());
        homeless.setRoom(StringUtils.isBlank(request.getRoom()) ? homeless.getRoom() : request.getRoom());
        homeless.setPhoneNumber(StringUtils.isBlank(request.getPhoneNumber()) ? homeless.getPhoneNumber() : PhoneNumberFormatter.format(request.getPhoneNumber()));
        homeless.setBirthDate(request.getBirthDate() == null ? homeless.getBirthDate() : request.getBirthDate());
        homeless.setAdmissionDate(request.getAdmissionDate() == null ? homeless.getAdmissionDate() : request.getAdmissionDate());
        homeless.setMemo(request.getMemo() == null ? homeless.getMemo() : request.getMemo());
    }

    @Transactional
    public void deleteHomeless(Long homelessId, Long shelterId) {
        homelessCommandService.deleteHomeless(homelessId, shelterId);
        locationTrackingService.deleteInoutStatus(homelessId, shelterId);
        noticeRecipientService.deleteAllByHomelessId(homelessId); //공지 사항 읽음 목록에서 삭제
    }

    public Long createChiefOfficer(Long shelterId, String name, String phoneNumber) {
        return chiefOfficerService.create(shelterId, name, phoneNumber);
    }

    public void updateChiefOfficer(Long shelterId, Long chiefOfficerId, String name, String phoneNumber) {
        chiefOfficerService.update(shelterId, chiefOfficerId, name, phoneNumber);
    }

    public void deleteChiefOfficer(Long shelterId, Long chiefOfficerId) {
        chiefOfficerService.delete(shelterId, chiefOfficerId);
    }

    public void createDutyOfficers(Long shelterId, List<DutyOfficerCreateRequest> requests) {
        dutyOfficerService.create(requests.stream().map(request -> DutyOfficer.builder().shelterId(shelterId).name(request.getName()).phoneNumber(PhoneNumberFormatter.format(request.getPhoneNumber())).targetDate(request.getTargetDate()).build()).toList());
    }

    public List<DutyOfficerResponse> getDutyOfficers(Long shelterId, LocalDate startDate, LocalDate endDate) {
        return dutyOfficerService.getList(shelterId, startDate, endDate).stream().map(dutyOfficer -> DutyOfficerResponse.builder().dutyOfficerId(dutyOfficer.getId()).name(dutyOfficer.getName()).phoneNumber(dutyOfficer.getPhoneNumber()).targetDate(dutyOfficer.getTargetDate()).build()).toList();
    }

    @Transactional
    public Long createHomeless(Long shelterId, CreateHomelessByAdminRequest request) {
        Shelter shelter = shelterService.getShelterById(shelterId).orElseThrow(() -> new IllegalArgumentException("id=" + shelterId + "인 센터가 존재하지 않습니다."));
        LocalDate targetDate = LocalDate.now();
        long result = homelessCommandService.create(Homeless.builder().name(request.getName()).shelter(shelter).deviceId(null).room(request.getRoom()).birthDate(request.getBirthDate()).phoneNumber(PhoneNumberFormatter.format(request.getPhoneNumber())).admissionDate(targetDate).memo(request.getMemo()).build());

        locationTrackingService.createOrUpdate(result, shelterId, InOutStatus.IN_SHELTER);

        return result;
    }

    public EmergencyResponse getEmergencyListByShelterId(Long shelterId) {
        List<EmergencyLog> allList = emergencyService.getEmergencyLogByShelterId(shelterId);

        List<EmergencyLogItem> list = new ArrayList<>();
        for (EmergencyLog log : allList) {
            EmergencyLogItem item = EmergencyLogItem.builder().id(log.getId()).name(log.getHomless().getName()).phNumber(log.getHomless().getPhoneNumber()).date(log.getCreatedAt()).location(Location.builder().lat(log.getLatitude()).lng(log.getLongitude()).build()).build();
            list.add(item);
        }

        return EmergencyResponse.builder().result(String.valueOf(list.size())).logs(list).build();
    }

    @Transactional
    public void updateHomelessInOutStatus(Long shelterId, Long homelessId, UpdateLocationRequest request) {
        homelessQueryService.getOneByIdAndShelter(homelessId, shelterId).orElseThrow(() -> new IllegalArgumentException("노숙인 정보가 존재하지 않습니다."));
        LocalDate targetDate = LocalDate.now();

        if (request.getLocationStatus() == InOutStatus.OUT_SHELTER && sleepoverService.isExist(homelessId, targetDate)) {
            request.setLocationStatus(InOutStatus.OVERNIGHT_STAY);
        }

        locationTrackingService.createOrUpdate(homelessId, shelterId, request.getLocationStatus());

        if (request.getLocationStatus() == InOutStatus.OUT_SHELTER) {
            dailyOutingCountsService.createOrUpdateDailyOutingCounts(shelterId, targetDate);
        }
    }

    public List<Long> monthlyHomelessCounts(Long shelterId, LocalDate targetDate) {
        if (targetDate == null) {
            targetDate = LocalDate.now();
        }
        Long currentCount = homelessQueryService.count(shelterId);
        dailyHomelessCountsService.createOrUpdate(shelterId, targetDate, currentCount);

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
        Long count = emergencyService.getCountByShelterIdAndDate(shelterId, targetDate);
        dailyEmergencyCountsService.createOrUpdate(shelterId, targetDate, count);
        return dailyEmergencyCountsService.getMonthlyCounts(shelterId, targetDate);
    }

    @Transactional
    public List<Long> monthlySleepoverCounts(Long shelterId, LocalDate targetDate) {
        if (targetDate == null) {
            targetDate = LocalDate.now();
        }
        LocalDate startDate = targetDate.withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        List<Sleepover> monthlySleeovers = sleepoverService.filterSleepoverShelterId(shelterId, startDate, endDate);

        Map<LocalDate, Long> countsByDate = new HashMap<>();

        // 외출 신청을 날짜별로 그룹화
        for (Sleepover sleepover : monthlySleeovers) {
            LocalDate start = sleepover.getStartDate().isBefore(startDate) ? startDate : sleepover.getStartDate();
            LocalDate end = sleepover.getEndDate().isAfter(endDate) ? endDate : sleepover.getEndDate();

            for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                countsByDate.merge(date, 1L, Long::sum);
            }
        }

        // 날짜별 카운트 반영 및 데이터베이스 업데이트
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            long count = countsByDate.getOrDefault(date, 0L);
            dailySleepoverCountsService.createOrUpdate(shelterId, date, count);
        }

        return dailySleepoverCountsService.getMonthlyCounts(shelterId, targetDate);
    }

    public Long createShelterAccount(ShelterAccountRequest request, Long shelterId) {
        ShelterAccount shelterAccount = ShelterAccount.builder().name(request.getName()).email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).phoneNumber(request.getPhoneNumber()).userRole(UserRole.SHELTER).shelterId(shelterId).remark(request.getRemark()).build();

        return shelterAccountService.save(shelterAccount);
    }

    public Long deleteShelterAccount(Long shelterId, Long accountId) {
        return shelterAccountService.deleteShelterAccount(shelterId, accountId);
    }

    public List<ShelterAccountResponse> getShelterAccountAll(Long shelterId) {
        List<ShelterAccount> shelterAccountList = shelterAccountService.getShelterAccountAll(shelterId);

        List<ShelterAccountResponse> resultList = new ArrayList<>();
        for (ShelterAccount account : shelterAccountList) {
            resultList.add(ShelterAccountResponse.builder().id(account.getId()).createdAt(account.getCreatedAt()).name(account.getName()).phoneNumber(account.getPhoneNumber()).remark(account.getRemark()).hasAdminRole(account.getUserRole() == UserRole.SHELTER_ADMIN).build());
        }

        return resultList;
    }


    @Transactional(rollbackFor = {Exception.class})
    public Long createNotice(Long shelterId, Long shelterAccountId, CreateNoticeRequest request) {

        boolean isGlobal = request.getTargetHomelessIds().isEmpty();

        Notice notice = Notice.builder().shelterId(shelterId).title(request.getTitle()).contents(request.getContent()).shelterAccountId(shelterAccountId)
                .imageUrl(request.getImageUrl())
                .isSurvey(request.getIsSurvey())
                .isGlobal(isGlobal)
                .build();

        Long noticeId = noticeService.save(notice);
        Set<Long> homelessIds = new HashSet<>(request.getTargetHomelessIds());
        if (request.getTargetHomelessIds().isEmpty()) {
            homelessIds = homelessQueryService.getHomelessIdsByShelterId(shelterId);
        }

        List<String> devicesId = new ArrayList<>();
        for (Long homelessId : homelessIds) {
            Optional<Homeless> homeless = homelessQueryService.getOneByIdAndShelter(homelessId, shelterId);
            if (homeless.isEmpty()) {
                throw new IllegalArgumentException("해당 센터에 존재하지 않는 노숙인 id가 존재합니다.");
            }
            devicesId.add(homeless.get().getDeviceId());
            NoticeRecipient noticeRecipient = NoticeRecipient.builder().shelterId(shelterId).noticeId(noticeId).homelessId(homelessId).build();
            noticeRecipientService.save(noticeRecipient);
        }

        String fcmStatus = request.getIsSurvey() ? "survey" : "notice";
        FcmMultiSendDto fcmMultiSendDto = FcmMultiSendDto.builder().tokens(devicesId).title(request.getTitle()).body(request.getContent()).data(FcmSendDto.Data.builder().screen(fcmStatus).noticeId(noticeId).build()).build();
        fcmService.sendMessageToMultiple(fcmMultiSendDto);

        return notice.getId();
    }

    public NumberPageResponse<NoticeResponse> getNoticePage(NoticePageRequest pageRequest) {
        return noticeFilterPagerProvider.from(pageRequest.getFilterType()).getPage(pageRequest);
    }

    public NoticeRecipientStatusResponse getNoticeRecipientStatus(Long noticeId, Long shelterId) {
        Map<Long, Boolean> homelessIdsAndReadStatus = noticeRecipientService.getAllHomelessIdAndReadByNoticeId(noticeId, shelterId);
        Map<Long, String[]> homelessInfo = homelessQueryService.getNameAndPhoneById(homelessIdsAndReadStatus.keySet().stream().toList());

        List<NoticeRecipientInfoResult> items = homelessIdsAndReadStatus.entrySet().stream().filter(entry -> homelessInfo.get(entry.getKey()) != null).map(entry -> {
            Long homelessId = entry.getKey();
            boolean isRead = entry.getValue();
            String[] info = homelessInfo.get(homelessId);
            return NoticeRecipientInfoResult.builder().homelessId(homelessId).homelessName(info[0]).homelessPhoneNumber(info[1] != null ? info[1] : "").isRead(isRead).build();
        }).toList();

        Long totalCount = (long) items.size();
        Long readCount = items.stream().filter(NoticeRecipientInfoResult::isRead).count();
        Long unReadCount = totalCount - readCount;

        return NoticeRecipientStatusResponse.builder().items(items).noticeReadInfo(NoticeRecipientStatusResponse.NoticeReadInfo.builder().totalCount(totalCount).readCount(readCount).unReadCount(unReadCount).build()).build();
    }

    public NoticeItemResponse getOneNotice(Long noticeId, Long shelterId) {
        Notice notice = noticeService.getOneByIdAndShelterId(noticeId, shelterId).orElseThrow(() -> new IllegalArgumentException("해당 id를 가진 공지사항이 없습니다."));
        NoticeItemResponse response = NoticeItemResponse.builder()
                .noticeId(notice.getId())
                .title(notice.getTitle())
                .contents(notice.getContents())
                .createdAt(notice.getCreatedAt())
                .imageUrl(notice.getImageUrl())
                .isSurvey(notice.getIsSurvey())
                .tags(List.of("전체인원", "미확인인원", "미참여인원"))
                .readHomelessIds(noticeRecipientService.getHomelessIdsByNoticeIdAndReadStatus(noticeId, true))
                .unreadHomelessIds(noticeRecipientService.getHomelessIdsByNoticeIdAndReadStatus(noticeId, false))
                .notParticipateHomelessIds(noticeRecipientService.getHomelessIdsByNoticeIdAndParticipateStatus(noticeId, ParticipateStatus.NOT_PARTICIPATE))
                .build();

        return response;
    }


    public HomelessInfoPageResponse<Object> getHomelessInfoPage(HomelessInfoPageRequest pageRequest) {
        return homelessInfoFilterPageProvider.from(pageRequest.getFilterType()).getPage(pageRequest);
    }

    public FileUploadResponse uploadFile(MultipartFile multipartFile) {
        return FileUploadResponse.builder()
                .imageUrl(awsS3Service.uploadImg(multipartFile))
                .build();
    }
}
