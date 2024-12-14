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
import org.wildflowergardening.backend.core.wildflowergardening.application.NotificationMessageService;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverService;
import org.wildflowergardening.backend.core.wildflowergardening.domain.NotificationMessage;
import org.wildflowergardening.backend.core.wildflowergardening.domain.NotificationMessageType;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RemindSleepoverTasklet implements Tasklet {
    private final SleepoverService sleepoverService;
    private final HomelessQueryService homelessQueryService;
    private final NotificationMessageService notificationMessageService;
    private final FCMService fcmService;

    //오전 7시 외박 당일 알림 서비스
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext context) {
        LocalDate targetDate = LocalDate.now();
        Set<Long> homelessIds = sleepoverService.getHomelessIdsByStartDate(targetDate);
        List<String> deviceIds = homelessQueryService.getDeviceIdsByHomelessIds(homelessIds);
        NotificationMessage notificationMessage = notificationMessageService.getMessageByType(NotificationMessageType.REMIND_SLEEPOVER).orElseThrow(() ->
                new IllegalArgumentException("Noticiation Type이 존재하지 않습니다"));

        FcmMultiSendDto fcmMultiSendDto = FcmMultiSendDto.builder()
                .tokens(deviceIds)
                .title(notificationMessage.getTitle())
                .body(notificationMessage.getContents())
                .data(FcmSendDto.Data.builder().screen("alarm").noticeId(null).build()).build();

        fcmService.sendMessageToMultiple(fcmMultiSendDto);

        return RepeatStatus.FINISHED;
    }
}
