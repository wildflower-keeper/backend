package org.wildflowergardening.backend.batch.tasklet;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.core.wildflowergardening.application.VerificationCodeService;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DeletedExpiredCodeTasklet implements Tasklet {

    private final VerificationCodeService verificationCodeService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LocalDateTime targetDate = LocalDateTime.now();
        verificationCodeService.deleteExpiredCode(targetDate);

        return RepeatStatus.FINISHED;
    }
}
