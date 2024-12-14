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
import org.wildflowergardening.backend.batch.tasklet.RemindReturnedTasklet;

@Configuration
@EnableBatchProcessing(dataSourceRef = "batchDataSource")
@RequiredArgsConstructor
public class RemindReturnJobConfig {
    private final JobRepository jobRepository;
    @Qualifier("batchTransactionManager")
    private final PlatformTransactionManager batchTransactionManager;
    private final RemindReturnedTasklet remindReturnedTasklet;

    @Bean
    public Job remindReturnJob() {
        return new JobBuilder("remindReturnJob", jobRepository)
                .start(remindReturnStep())
                .build();
    }

    @Bean
    public Step remindReturnStep() {
        return new StepBuilder("remindReturnStep", jobRepository)
                .tasklet(remindReturnedTasklet, batchTransactionManager)
                .build();
    }

}
