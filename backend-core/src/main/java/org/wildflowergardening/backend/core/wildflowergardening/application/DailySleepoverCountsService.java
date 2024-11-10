package org.wildflowergardening.backend.core.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.DailyOutingCounts;
import org.wildflowergardening.backend.core.wildflowergardening.domain.DailySleepoverCounts;
import org.wildflowergardening.backend.core.wildflowergardening.domain.DailySleepoverCountsRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailySleepoverCountsService {
    private final DailySleepoverCountsRepository dailySleepoverCountsRepository;

    @Transactional
    public Long create(Long shelterId, Long count, LocalDate now) {
        return dailySleepoverCountsRepository.save(DailySleepoverCounts.builder()
                .shelterId(shelterId)
                .recordedDate(now)
                .count(count)
                .build()).getId();
    }

    @Transactional(readOnly = true)
    public List<Long> getMonthlyCounts(Long shelterId, LocalDate now) {
        LocalDate startDate = LocalDate.of(now.getYear(), now.getMonth(), 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        return dailySleepoverCountsRepository.findMonthlyCountsByShelterIdAndDateRange(shelterId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public Optional<DailySleepoverCounts> getSleepoverCountsByDate(Long shelterId, LocalDate now) {
        return dailySleepoverCountsRepository.findByShelterIdAndRecordedDate(shelterId, now);
    }
}
