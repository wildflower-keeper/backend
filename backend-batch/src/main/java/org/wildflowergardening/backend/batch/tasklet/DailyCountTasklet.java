package org.wildflowergardening.backend.batch.tasklet;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.core.wildflowergardening.application.*;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Shelter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Sleepover;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DailyCountTasklet implements Tasklet {
    private final DailyHomelessCountsService dailyHomelessCountsService;
    private final DailyOutingCountsService dailyOutingCountsService;
    private final DailySleepoverCountsService dailySleepoverCountsService;
    private final DailyEmergencyCountsService dailyEmergencyCountsService;
    private final ShelterService shelterService;
    private final HomelessQueryService homelessQueryService;
    private final SleepoverService sleepoverService;
    private final EmergencyService emergencyService;


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext context) {
        LocalDate targetDate = LocalDate.now();
        List<Shelter> shelterList = shelterService.getAll();
        LocalDate startDate = targetDate.withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        for (Shelter shelter : shelterList) {

            //노숙인 수
            Long homelessCount = homelessQueryService.count(shelter.getId());
            dailyHomelessCountsService.createOrUpdate(shelter.getId(), targetDate, homelessCount);

            dailyOutingCountsService.getOrCreateDailyOutingCounts(shelter.getId(), targetDate);

            //긴급 상황 수
            Long emergencyCount = emergencyService.getCountByShelterIdAndDate(shelter.getId(), targetDate);
            dailyEmergencyCountsService.createOrUpdate(shelter.getId(), targetDate, emergencyCount);


            //-- 외박 신청 내역을 바탕으로 외박 신청 count 설정
            List<Sleepover> monthlySleeovers = sleepoverService.filterSleepoverShelterId(shelter.getId(), startDate, endDate);

            Map<LocalDate, Long> countsByDate = new HashMap<>();

            for (Sleepover sleepover : monthlySleeovers) {
                LocalDate start = sleepover.getStartDate().isBefore(startDate) ? startDate : sleepover.getStartDate();
                LocalDate end = sleepover.getEndDate().isAfter(endDate) ? endDate : sleepover.getEndDate();

                for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                    countsByDate.merge(date, 1L, Long::sum);
                }
            }

            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                long count = countsByDate.getOrDefault(date, 0L);
                dailySleepoverCountsService.createOrUpdate(shelter.getId(), date, count);
            }


        }
        return RepeatStatus.FINISHED;
    }
}
