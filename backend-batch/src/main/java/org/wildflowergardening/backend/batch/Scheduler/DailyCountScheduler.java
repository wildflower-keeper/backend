package org.wildflowergardening.backend.batch.Scheduler;

import lombok.RequiredArgsConstructor;
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

    @Scheduled(cron = "0 0 0 * * ?")  // 매일 정오에 실행
    public void runDailyCountJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(dailyCountJob, jobParameters);
    }
}
