package org.wildflowergardening.backend.core.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.DailyEmergencyCounts;
import org.wildflowergardening.backend.core.wildflowergardening.domain.DailyEmergencyCountsRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyEmergencyCountsService {
    private final DailyEmergencyCountsRepository dailyEmergencyCountsRepository;

    @Transactional
    public Long create(Long shelterId, Long count, LocalDate now) {
        return dailyEmergencyCountsRepository.save(DailyEmergencyCounts.builder()
                .shelterId(shelterId)
                .recordedDate(now)
                .count(count)
                .build()).getId();
    }

    @Transactional
    public DailyEmergencyCounts getOrCreateDailyEmergencyCounts(Long shelterId, LocalDate targetDate) {
        Optional<DailyEmergencyCounts> countsOptional = dailyEmergencyCountsRepository.findByShelterIdAndRecordedDate(shelterId, targetDate);
        return countsOptional.orElseGet(() -> DailyEmergencyCounts.builder()
                .shelterId(shelterId)
                .recordedDate(targetDate)
                .build());
    }

    @Transactional(readOnly = true)
    public List<Long> getMonthlyCounts(Long shelterId, LocalDate now) {
        LocalDate startDate = LocalDate.of(now.getYear(), now.getMonth(), 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        List<DailyEmergencyCounts> dailyEmergencyList = dailyEmergencyCountsRepository.findByShelterIdAndRecordedDateIsBetween(shelterId, startDate, endDate);
        List<Long> result = new ArrayList<>(Collections.nCopies(endDate.getDayOfMonth(), 0L));

        for (DailyEmergencyCounts count : dailyEmergencyList) {
            int dayOfMonth = count.getRecordedDate().getDayOfMonth();
            result.set(dayOfMonth - 1, count.getCount());
        }

        return result;
    }

    @Transactional(readOnly = true)
    public Optional<DailyEmergencyCounts> getEmergencyCountsByDate(Long shelterId, LocalDate now) {
        return dailyEmergencyCountsRepository.findByShelterIdAndRecordedDate(shelterId, now);
    }
}
