package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.CreateSleepoverDto;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult.PageInfoResult;
import org.wildflowergardening.backend.core.wildflowergardening.domain.*;

@Service
@RequiredArgsConstructor
public class SleepoverService {

    private final HomelessRepository homelessRepository;
    private final SleepoverRepository sleepoverRepository;

    @Transactional
    public Long create(CreateSleepoverDto dto) {
        // 외박 기간 중복 검사
        List<Sleepover> overlapped = sleepoverRepository.findAllByHomelessAndPeriod(
                dto.getHomelessId(), dto.getEndDate(), dto.getStartDate()
        );
        if (!overlapped.isEmpty()) {
            throw new IllegalArgumentException("기간이 중복되는 외박 신청 내역이 있습니다.");
        }
        // 외박 신청 create
        Homeless homeless = homelessRepository.findById(dto.getHomelessId())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        Sleepover sleepover = Sleepover.builder()
                .creatorType(dto.getCreatorType())
                .shelterId(dto.getShelterId())
                .homelessId(homeless.getId())
                .homelessName(homeless.getName())
                .homelessPhoneNumber(homeless.getPhoneNumber())
                .homelessRoom(homeless.getRoom())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .reason(dto.getReason())
                .emergencyContact(dto.getEmergencyContact())
                .build();
        return sleepoverRepository.save(sleepover).getId();
    }

    @Transactional(readOnly = true)
    public List<Sleepover> getSleepoversForPeriod(
            Long homelessId, LocalDate periodStart, LocalDate periodEnd
    ) {
        return sleepoverRepository.findAllByHomelessAndPeriod(
                homelessId, periodEnd, periodStart);
    }


    /**
     * @param candidateHomelessIds targetDate 에 외박신청했는지 알고싶은 노숙인 ids
     * @param sleepoverTargetDate  외박 신청 확인 기준일
     * @return candidateHomelessIds 중에서 오늘 외박 신청한 노숙인 ids
     */
    @Transactional(readOnly = true)
    public Set<Long> filterSleepoverHomelessIds(
            List<Long> candidateHomelessIds, LocalDate sleepoverTargetDate
    ) {
        return sleepoverRepository.filterSleepoverHomeless(
                candidateHomelessIds, sleepoverTargetDate
        );
    }

    @Transactional(readOnly = true)
    public List<Sleepover> filterSleepoverShelterId(
            Long shelterId, LocalDate startDate, LocalDate endDate
    ) {
        return sleepoverRepository.filterSleepoverByShelterId(
                shelterId, startDate, endDate
        );
    }

    /*
     외박신청 전체 목록 조회 (최신순)
     */
    @Transactional(readOnly = true)
    public NumberPageResult<Sleepover> getPage(
            Long shelterId, int pageNumber, int pageSize
    ) {
        PageRequest pageRequest = PageRequest.of(
                pageNumber - 1, pageSize, Sort.by(Direction.DESC, "id")
        );
        Page<Sleepover> sleepoverPage = sleepoverRepository.findAllByShelterId(shelterId, pageRequest);

        return NumberPageResult.<Sleepover>builder()
                .items(sleepoverPage.getContent())
                .pagination(PageInfoResult.of(sleepoverPage))
                .build();
    }

    @Transactional(readOnly = true)
    public Map<Long, Sleepover> getSleepoverByHomelessIds(List<Long> homelessIds, LocalDate date) {
        List<Sleepover> sleepovers = sleepoverRepository.filterSleepoverByHomelessIds(homelessIds, date);

        return sleepovers.stream().collect(Collectors.toMap(
                Sleepover::getHomelessId,
                Function.identity()
        ));
    }

    @Transactional(readOnly = true)
    public Optional<Sleepover> getUpcomingSleepover(Long homelessId, LocalDate today) {
        return sleepoverRepository.findTopByHomelessIdAndEndDateAfterAndDeletedAtIsNullOrderByStartDateAsc(
                homelessId, today);
    }

    @Transactional(readOnly = true)
    public List<Sleepover> getAllSleepoversEndDateAfter(Long homelessId, LocalDate targetDate) {
        return sleepoverRepository.findByHomelessAndEndDateAfter(homelessId, targetDate);
    }

    /*
     특정 날짜에 외박하겠다고 되어있는 외박신청 목록 조회
     */
    @Transactional(readOnly = true)
    public NumberPageResult<Sleepover> getPage(
            Long shelterId, LocalDate sleepoverTargetDate, int pageNumber, int pageSize
    ) {
        PageRequest pageRequest = PageRequest.of(
                pageNumber - 1, pageSize, Sort.by(Direction.DESC, "id")
        );
        Page<Sleepover> sleepoverPage = sleepoverRepository.findAllByShelterIdAndTargetDate(
                shelterId, sleepoverTargetDate, pageRequest
        );
        return NumberPageResult.<Sleepover>builder()
                .items(sleepoverPage.getContent())
                .pagination(PageInfoResult.of(sleepoverPage))
                .build();
    }

    @Transactional(readOnly = true)
    public long count(Long shelterId, LocalDate sleepoverTargetDate) {
        Long count = sleepoverRepository.countByTargetDate(shelterId, sleepoverTargetDate);
        return count != null ? count : 0;
    }

    @Transactional
    public Sleepover delete(Long homelessId, Long sleepoverId) {
        Sleepover sleepover = sleepoverRepository.findByIdAndHomelessId(sleepoverId, homelessId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "id=" + sleepoverId + "인 외박신청 내역이 존재하지 않습니다."));

        if (!sleepover.cancelableAt(LocalDate.now())) {
            throw new IllegalArgumentException("기간이 진행된 외박신청을 취소할 수 없습니다.");
        }
        sleepover.toSoftDeleted();
        return sleepover;
    }

    @Transactional(readOnly = true)
    public List<Sleepover> findAllByCreatedAtIn(
            Long shelterId, LocalDateTime createdAtStart, LocalDateTime createdAtEnd
    ) {
        return sleepoverRepository.findAllByCreatedAtIn(shelterId, createdAtStart, createdAtEnd);
    }

    @Transactional(readOnly = true)
    public boolean isExist(Long homelessId, LocalDate targetDate) {
        Set<Long> existHomelessId = sleepoverRepository.filterSleepoverHomeless(
                Collections.singletonList(homelessId), targetDate
        );
        return !existHomelessId.isEmpty();
    }

    @Transactional(readOnly = true)
    public Set<Long> getSleepoverEndedHomelessIds(LocalDate now) {
        LocalDate yesterday = now.minusDays(1);
        return sleepoverRepository.findHomelessIdsWithSleepoverEndingYesterday(yesterday);
    }

    @Transactional(readOnly = true)
    public Set<Long> getSleepOverHomelessIds(LocalDate now) {
        return sleepoverRepository.findBySleepoverHomeless(now);
    }

    @Transactional(readOnly = true)
    public Set<Long> getHomelessIdsByStartDate(LocalDate targetDate) {
        return sleepoverRepository.findHomelessIdsByStartDate(targetDate);
    }

    @Transactional(readOnly = true)
    public Map<Long, Sleepover> getAll(List<Long> homelessIds) {
        List<Sleepover> sleepoverList = sleepoverRepository.findByHomelessIdIn(homelessIds);
        return sleepoverList.stream().collect(Collectors.toMap(
                Sleepover::getHomelessId,
                Function.identity()
        ));
    }
}
