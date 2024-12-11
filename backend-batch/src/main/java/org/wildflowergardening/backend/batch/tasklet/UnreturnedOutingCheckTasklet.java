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
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UnreturnedOutingCheckTasklet implements Tasklet {
    private final LocationTrackingService locationTrackingService;

    //아침 7시에 도는 것
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
        List<LocationTracking> locationTrackingList = locationTrackingService.getAllByInOutStatus(InOutStatus.OUT_SHELTER);
        for (LocationTracking locationTracking : locationTrackingList) {
            locationTracking.setInOutStatus(InOutStatus.UNCONFIRMED);
        }

        return RepeatStatus.FINISHED;
    }

}
