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
public class DeleteExpiredSessionScheduler {
    private final JobLauncher jobLauncher;
    private final Job deleteExpiredSessionJob;

    @Scheduled(cron = "0 0 4 * * ?")  // 매일 새벽 4시에 실행
    public void runDeleteExpiredSessionJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(deleteExpiredSessionJob, jobParameters);
    }
}
