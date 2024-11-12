package org.wildflowergardening.backend.batch.tasklet;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.core.wildflowergardening.application.LocationTrackingService;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverService;
import org.wildflowergardening.backend.core.wildflowergardening.domain.InOutStatus;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationTracking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UnreturnedOvernightStayTasklet implements Tasklet {

    private final SleepoverService sleepoverService;
    private final LocationTrackingService locationTrackingService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LocalDate now = LocalDate.now();
        Set<Long> sleepoverEndedHomelessIds = sleepoverService.getSleepoverEndedHomelessIds(now);
        List<LocationTracking> unReturned = locationTrackingService.getUnreturnedSleepoversHomelessIds(sleepoverEndedHomelessIds, LocalDateTime.now());
        for (LocationTracking location : unReturned) {
            location.setInOutStatus(InOutStatus.UNCONFIRMED);
        }

        return RepeatStatus.FINISHED;
    }
}
