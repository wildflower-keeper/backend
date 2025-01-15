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
import java.util.List;

@Component
@RequiredArgsConstructor
public class UnreturnedOvernightStayTasklet implements Tasklet {

    private final SleepoverService sleepoverService;
    private final LocationTrackingService locationTrackingService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<LocationTracking> locationTrackingList = locationTrackingService.getAllByInOutStatus(InOutStatus.OVERNIGHT_STAY);
        for (LocationTracking locationTracking : locationTrackingList) {
            if (sleepoverService.isExist(locationTracking.getHomelessId(), LocalDate.now())) continue;
            locationTracking.setInOutStatus(InOutStatus.UNCONFIRMED);
        }

        return RepeatStatus.FINISHED;
    }
}
