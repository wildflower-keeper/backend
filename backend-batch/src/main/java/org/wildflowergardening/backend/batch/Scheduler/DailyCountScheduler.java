package org.wildflowergardening.backend.batch.Scheduler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@EnableScheduling
@RequiredArgsConstructor
public class DailyCountScheduler {

    private final JobLauncher jobLauncher;
    private final Job dailyCountJob;
    private static final Logger logger = LoggerFactory.getLogger(LocationStatusCheckScheduler.class);

    @Scheduled(cron = "0 0 0 * * ?")  // 매일 정오에 실행
    public void runDailyCountJob() throws Exception {
        logger.info("starting dailyCountJob");
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(dailyCountJob, jobParameters);
        logger.info("finished dailyCountJob");
    }
}
