package org.wildflowergardening.backend.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.wildflowergardening.backend.batch.tasklet.UnreturnedOutingCheckTasklet;

@Configuration
@EnableBatchProcessing(dataSourceRef = "batchDataSource")
@RequiredArgsConstructor
public class UnreturnedOutingCheckJobConfig {
    private final JobRepository jobRepository;
    @Qualifier("batchTransactionManager")
    private final PlatformTransactionManager batchTransactionManager;
    private final UnreturnedOutingCheckTasklet unreturnedOutingCheckTasklet;

    @Bean
    public Job unreturnedOutingCheckJob() {
        return new JobBuilder("unreturnedOutingCheckJob", jobRepository)
                .start(outingUnReturnCheckStep())
                .build();
    }

    @Bean
    public Step outingUnReturnCheckStep() {
        return new StepBuilder("outingUnReturnCheckStep", jobRepository)
                .tasklet(unreturnedOutingCheckTasklet, batchTransactionManager)
                .build();
    }
}
