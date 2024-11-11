package org.wildflowergardening.backend.core.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.DailyHomelessCounts;
import org.wildflowergardening.backend.core.wildflowergardening.domain.DailyHomelessCountsRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyHomelessCountsService {

    private final DailyHomelessCountsRepository dailyHomelessCountsRepository;

    @Transactional
    public DailyHomelessCounts getOrCreateDailyHomelessCount(Long shelterId, LocalDate targetDate) {
        return dailyHomelessCountsRepository.findByShelterIdAndRecordedDate(shelterId, targetDate)
                .orElseGet(() -> {
                    DailyHomelessCounts newCount = DailyHomelessCounts.builder()
                            .shelterId(shelterId)
                            .recordedDate(targetDate)
                            .count(0L)
                            .build();
                    return dailyHomelessCountsRepository.save(newCount);
                });
    }

    @Transactional(readOnly = true)
    public List<Long> getMonthlyCounts(Long shelterId, LocalDate now) {
        LocalDate startDate = LocalDate.of(now.getYear(), now.getMonth(), 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        List<DailyHomelessCounts> dailyHomelessCountsList = dailyHomelessCountsRepository.findByShelterIdAndRecordedDateIsBetween(shelterId, startDate, endDate);
        List<Long> result = new ArrayList<>(Collections.nCopies(endDate.getDayOfMonth(), 0L));

        for (DailyHomelessCounts count : dailyHomelessCountsList) {
            int dayOfMonth = count.getRecordedDate().getDayOfMonth();
            result.set(dayOfMonth - 1, count.getCount());
        }

        return result;
    }

    @Transactional(readOnly = true)
    public Optional<DailyHomelessCounts> getHomelessCountsByDate(Long shelterId, LocalDate now) {
        return dailyHomelessCountsRepository.findByShelterIdAndRecordedDate(shelterId, now);
    }

}
