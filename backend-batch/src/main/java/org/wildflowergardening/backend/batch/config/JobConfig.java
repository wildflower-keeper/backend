package org.wildflowergardening.backend.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class JobConfig {

  @Autowired
  private JobRepository jobRepository;

  @Autowired
  private PlatformTransactionManager platformTransactionManager;

  @Bean
  public Job testJob() {
    return new JobBuilder("testJob", jobRepository)
        .start(testStep())
        .build();
  }

  @Bean
  public Step testStep() {
    return new StepBuilder("testStep", jobRepository)
        .tasklet(testTasklet(), platformTransactionManager)
        .build();
  }

  @Bean
  public Tasklet testTasklet() {
    return (contribution, chunkContext) -> {
      log.debug("## test batch tasklet executed");
      return RepeatStatus.FINISHED;
    };
  }
}
