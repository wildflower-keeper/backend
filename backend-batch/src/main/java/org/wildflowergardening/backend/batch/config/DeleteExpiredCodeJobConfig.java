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
import org.wildflowergardening.backend.batch.tasklet.DeletedExpiredCodeTasklet;

@Configuration
@EnableBatchProcessing(dataSourceRef = "batchDataSource")
@RequiredArgsConstructor
public class DeleteExpiredCodeJobConfig {

    private final JobRepository jobRepository;
    @Qualifier("batchTransactionManager")
    private final PlatformTransactionManager batchTransactionManager;
    private final DeletedExpiredCodeTasklet deletedExpiredCodeTasklet;

    @Bean
    public Job deleteExpiredCodeJob(){
        return new JobBuilder("deleteExpiredCodeJob", jobRepository)
                .start(deleteExpiredCodeStep())
                .build();
    }

    @Bean
    public Step deleteExpiredCodeStep() {
        return new StepBuilder("deleteExpiredCodeStep", jobRepository)
                .tasklet(deletedExpiredCodeTasklet, batchTransactionManager)
                .build();
    }
}
