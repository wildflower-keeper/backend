package org.wildflowergardening.backend.core.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.DailyOutingCounts;
import org.wildflowergardening.backend.core.wildflowergardening.domain.DailyOutingCountsRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyOutingCountsService {

    private final DailyOutingCountsRepository dailyOutingCountsRepository;

    @Transactional
    public Long create(Long shelterId, Long count, LocalDate now) {
        return dailyOutingCountsRepository.save(DailyOutingCounts.builder()
                .shelterId(shelterId)
                .recordedDate(now)
                .count(count)
                .build()).getId();
    }

    @Transactional
    public DailyOutingCounts getOrCreateDailyOutingCounts(Long shelterId, LocalDate targetDate) {
        return dailyOutingCountsRepository.findByShelterIdAndRecordedDate(shelterId, targetDate)
                .orElseGet(() -> {
                    DailyOutingCounts newCount = DailyOutingCounts.builder()
                            .shelterId(shelterId)
                            .recordedDate(targetDate)
                            .count(0L)
                            .build();
                    return dailyOutingCountsRepository.save(newCount);
                });
    }

    @Transactional(readOnly = true)
    public List<Long> getMonthlyCounts(Long shelterId, LocalDate now) {
        LocalDate startDate = LocalDate.of(now.getYear(), now.getMonth(), 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        List<DailyOutingCounts> dailyOutingCountsList = dailyOutingCountsRepository.findByShelterIdAndRecordedDateIsBetween(shelterId, startDate, endDate);
        List<Long> result = new ArrayList<>(Collections.nCopies(endDate.getDayOfMonth(), 0L));

        for (DailyOutingCounts count : dailyOutingCountsList) {
            int dayOfMonth = count.getRecordedDate().getDayOfMonth();
            result.set(dayOfMonth - 1, count.getCount());
        }

        return result;
    }

    @Transactional(readOnly = true)
    public Optional<DailyOutingCounts> getOutingCountsByDate(Long shelterId, LocalDate now) {
        return dailyOutingCountsRepository.findByShelterIdAndRecordedDate(shelterId, now);
    }
}
