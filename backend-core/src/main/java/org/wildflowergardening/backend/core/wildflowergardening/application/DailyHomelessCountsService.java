package org.wildflowergardening.backend.core.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.DailyHomelessCounts;
import org.wildflowergardening.backend.core.wildflowergardening.domain.DailyHomelessCountsRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyHomelessCountsService {

    private final DailyHomelessCountsRepository dailyHomelessCountsRepository;

    @Transactional
    public Long create(Long shelterId, Long count, LocalDate now) {
        return dailyHomelessCountsRepository.save(DailyHomelessCounts.builder()
                .shelterId(shelterId)
                .recordedDate(now)
                .count(count)
                .build()).getId();
    }

    @Transactional(readOnly = true)
    public List<Long> getMonthlyCounts(Long shelterId, LocalDate now) {
        LocalDate startDate = LocalDate.of(now.getYear(), now.getMonth(), 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        return dailyHomelessCountsRepository.findMonthlyCountsByShelterIdAndDateRange(shelterId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public Optional<DailyHomelessCounts> getHomelessCountsByDate(Long shelterId, LocalDate now) {
        return dailyHomelessCountsRepository.findByShelterIdAndRecordedDate(shelterId, now);
    }

}
