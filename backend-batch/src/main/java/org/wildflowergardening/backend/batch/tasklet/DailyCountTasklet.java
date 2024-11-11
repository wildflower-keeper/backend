package org.wildflowergardening.backend.batch.tasklet;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.core.wildflowergardening.application.*;
import org.wildflowergardening.backend.core.wildflowergardening.domain.DailyHomelessCounts;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Shelter;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DailyCountTasklet implements Tasklet {
    private final DailyHomelessCountsService dailyHomelessCountsService;
    private final DailyOutingCountsService dailyOutingCountsService;
    private final DailySleepoverCountsService dailySleepoverCountsService;
    private final DailyEmergencyCountsService dailyEmergencyCountsService;
    private final ShelterService shelterService;
    private final HomelessQueryService homelessQueryService;
    

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext context) {
        LocalDate targetDate = LocalDate.now();
        List<Shelter> shelterList = shelterService.getAll();
        for (Shelter shelter : shelterList) {
            DailyHomelessCounts dailyHomelessCounts = dailyHomelessCountsService.getOrCreateDailyHomelessCount(shelter.getId(), targetDate);
            Long count = homelessQueryService.count(shelter.getId());
            dailyHomelessCounts.setCount(count);
            dailyOutingCountsService.getOrCreateDailyOutingCounts(shelter.getId(), targetDate);
            dailyEmergencyCountsService.getOrCreateDailyEmergencyCounts(shelter.getId(), targetDate);
            dailySleepoverCountsService.getOrCreateDailySleepoverCounts(shelter.getId(), targetDate);
        }
        return RepeatStatus.FINISHED;
    }
}
