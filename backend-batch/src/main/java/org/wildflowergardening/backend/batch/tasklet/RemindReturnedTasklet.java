package org.wildflowergardening.backend.batch.tasklet;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.core.kernel.config.FCMService;
import org.wildflowergardening.backend.core.kernel.config.dto.FcmMultiSendDto;
import org.wildflowergardening.backend.core.kernel.config.dto.FcmSendDto;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessQueryService;
import org.wildflowergardening.backend.core.wildflowergardening.application.LocationTrackingService;
import org.wildflowergardening.backend.core.wildflowergardening.application.NotificationMessageService;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverService;
import org.wildflowergardening.backend.core.wildflowergardening.domain.InOutStatus;
import org.wildflowergardening.backend.core.wildflowergardening.domain.NotificationMessage;
import org.wildflowergardening.backend.core.wildflowergardening.domain.NotificationMessageType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RemindReturnedTasklet implements Tasklet {

    private final SleepoverService sleepoverService;
    private final LocationTrackingService locationTrackingService;
    private final HomelessQueryService homelessQueryService;
    private final NotificationMessageService notificationMessageService;
    private final FCMService fcmService;


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        Set<Long> homelessIds = locationTrackingService.getHomelessIdsByStatuses(
                new ArrayList<>(Arrays.asList(InOutStatus.OUT_SHELTER, InOutStatus.OVERNIGHT_STAY, InOutStatus.UNCONFIRMED))
        );
        homelessIds.removeIf(homelessId -> sleepoverService.isExist(homelessId, LocalDate.now()));
        List<String> deviceIds = homelessQueryService.getDeviceIdsByHomelessIds(homelessIds);
        NotificationMessage notificationMessage = notificationMessageService.getMessageByType(NotificationMessageType.REMIND_RETURN)
                .orElseThrow(() -> new IllegalArgumentException("Notification Type이 존재하지 않습니다."));

        FcmMultiSendDto fcmMultiSendDto = FcmMultiSendDto.builder()
                .tokens(deviceIds)
                .title(notificationMessage.getTitle())
                .body(notificationMessage.getContents())
                .data(FcmSendDto.Data.builder().screen("alarm").noticeId(null).build()).build();

        fcmService.sendMessageToMultiple(fcmMultiSendDto);

        return RepeatStatus.FINISHED;
    }
}
