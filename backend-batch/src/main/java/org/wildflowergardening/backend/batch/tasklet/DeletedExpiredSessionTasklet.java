package org.wildflowergardening.backend.batch.tasklet;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.core.wildflowergardening.application.SessionService;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DeletedExpiredSessionTasklet implements Tasklet {

    private final SessionService sessionService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LocalDateTime targetDate = LocalDateTime.now();
        sessionService.deleteExpiredCode(targetDate);

        return RepeatStatus.FINISHED;
    }
}
