package org.wildflowergardening.backend.core.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.DailyHomelessCounts;
import org.wildflowergardening.backend.core.wildflowergardening.domain.DailyHomelessCountsRepository;
import org.wildflowergardening.backend.core.wildflowergardening.domain.InOutStatus;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationTracking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyHomelessCountsService {

    private final DailyHomelessCountsRepository dailyHomelessCountsRepository;

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

    @Transactional
    public Long createOrUpdate(Long shelterId, LocalDate date, Long count) {
        Optional<DailyHomelessCounts> dailyHomelessCountsOptional =
                dailyHomelessCountsRepository.findByShelterIdAndRecordedDate(shelterId, date);

        if (dailyHomelessCountsOptional.isEmpty()) {
            return dailyHomelessCountsRepository.save(DailyHomelessCounts.builder()
                    .count(count)
                    .shelterId(shelterId)
                    .recordedDate(date)
                    .build()).getCount();
        }

        DailyHomelessCounts dailyHomelessCounts = dailyHomelessCountsOptional.get();
        dailyHomelessCounts.setCount(count);

        return dailyHomelessCounts.getCount();
    }
}
