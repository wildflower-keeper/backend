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
import org.wildflowergardening.backend.batch.tasklet.SleepOverCheckTasklet;
import org.wildflowergardening.backend.batch.tasklet.UnreturnedOutingCheckTasklet;
import org.wildflowergardening.backend.batch.tasklet.UnreturnedOvernightStayTasklet;

@Configuration
@EnableBatchProcessing(dataSourceRef = "batchDataSource")
@RequiredArgsConstructor
public class LocationStatusCheckJobConfig {
    private final JobRepository jobRepository;

    @Qualifier("batchTransactionManager")
    private final PlatformTransactionManager batchTransactionManager;
    private final UnreturnedOutingCheckTasklet unreturnedOutingCheckTasklet;
    private final UnreturnedOvernightStayTasklet unreturnedOvernightStayTasklet;

    @Bean
    public Job locationStatusCheckJob() {
        return new JobBuilder("locationStatusCheckJob", jobRepository)
                .start(outingUnReturnCheckStep())
                .next(overnightUnReturnCheckStep())
                .build();
    }

    @Bean
    public Step outingUnReturnCheckStep() {
        return new StepBuilder("outingUnReturnCheckStep", jobRepository)
                .tasklet(unreturnedOutingCheckTasklet, batchTransactionManager)
                .build();
    }

    @Bean
    public Step overnightUnReturnCheckStep() {
        return new StepBuilder("overnightUnReturnCheckStep", jobRepository)
                .tasklet(unreturnedOvernightStayTasklet, batchTransactionManager)
                .build();
    }

}
