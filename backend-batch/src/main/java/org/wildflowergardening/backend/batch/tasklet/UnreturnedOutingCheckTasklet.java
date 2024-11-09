package org.wildflowergardening.backend.batch.tasklet;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.core.wildflowergardening.application.LocationTrackingService;
import org.wildflowergardening.backend.core.wildflowergardening.domain.InOutStatus;
import org.wildflowergardening.backend.core.wildflowergardening.domain.LocationTracking;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UnreturnedOutingCheckTasklet implements Tasklet {
    private final LocationTrackingService locationTrackingService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        List<LocationTracking> noReturnOutings = locationTrackingService.getUnreturnedOutingsHomelessIds(now);

        for (LocationTracking locationTracking : noReturnOutings) {
            locationTracking.setInOutStatus(InOutStatus.UNCONFIRMED);
        }

        return RepeatStatus.FINISHED;
    }

}
