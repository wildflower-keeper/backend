package org.wildflowergardening.backend.core.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.DailySleepoverCounts;
import org.wildflowergardening.backend.core.wildflowergardening.domain.DailySleepoverCountsRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailySleepoverCountsService {
    private final DailySleepoverCountsRepository dailySleepoverCountsRepository;

    @Transactional
    public Long create(Long shelterId, LocalDate now) {
        return dailySleepoverCountsRepository.save(DailySleepoverCounts.builder()
                .shelterId(shelterId)
                .recordedDate(now)
                .build()).getId();
    }

    @Transactional
    public DailySleepoverCounts getOrCreateDailySleepoverCounts(Long shelterId, LocalDate targetDate) {
        return dailySleepoverCountsRepository.findByShelterIdAndRecordedDate(shelterId, targetDate)
                .orElseGet(() -> {
                    DailySleepoverCounts newCount = DailySleepoverCounts.builder()
                            .shelterId(shelterId)
                            .recordedDate(targetDate)
                            .count(0L)
                            .build();
                    return dailySleepoverCountsRepository.save(newCount);
                });
    }

    @Transactional(readOnly = true)
    public List<Long> getMonthlyCounts(Long shelterId, LocalDate now) {
        LocalDate startDate = LocalDate.of(now.getYear(), now.getMonth(), 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        List<DailySleepoverCounts> dailySleepoverCountsList = dailySleepoverCountsRepository.findByShelterIdAndRecordedDateIsBetween(shelterId, startDate, endDate);
        List<Long> result = new ArrayList<>(Collections.nCopies(endDate.getDayOfMonth(), 0L));

        for (DailySleepoverCounts count : dailySleepoverCountsList) {
            int dayOfMonth = count.getRecordedDate().getDayOfMonth();
            result.set(dayOfMonth - 1, count.getCount());
        }

        return result;
    }

    @Transactional(readOnly = true)
    public Optional<DailySleepoverCounts> getSleepoverCountsByDate(Long shelterId, LocalDate now) {
        return dailySleepoverCountsRepository.findByShelterIdAndRecordedDate(shelterId, now);
    }
}
