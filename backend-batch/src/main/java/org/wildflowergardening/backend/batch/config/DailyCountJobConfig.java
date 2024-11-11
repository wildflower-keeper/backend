package org.wildflowergardening.backend.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.wildflowergardening.backend.batch.tasklet.DailyCountTasklet;
import org.wildflowergardening.backend.batch.tasklet.UnreturnedOutingCheckTasklet;
import org.wildflowergardening.backend.batch.tasklet.UnreturnedOvernightStayTasklet;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class DailyCountJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DailyCountTasklet dailyCountTasklet;

    @Bean
    public Job dailyCountJob() {
        return new JobBuilder("dailyCountJob", jobRepository)
                .start(createDailyCountsStep())
                .build();
    }

    @Bean
    public Step createDailyCountsStep() {
        return new StepBuilder("createDailyCountsStep", jobRepository)
                .tasklet(dailyCountTasklet, transactionManager)
                .build();
    }
}
