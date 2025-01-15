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
public class UndefinedToInShelterTasklet implements Tasklet {

    private final SleepoverService sleepoverService;
    private final LocationTrackingService locationTrackingService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<LocationTracking> locationTrackingList = locationTrackingService.getAllByInOutStatusIn24Hours(InOutStatus.UNCONFIRMED);
        for (LocationTracking locationTracking : locationTrackingList) {
            if (sleepoverService.isExist(locationTracking.getHomelessId(), LocalDate.now())) continue;
            locationTracking.setInOutStatus(InOutStatus.IN_SHELTER);
        }
        return RepeatStatus.FINISHED;
    }
}
