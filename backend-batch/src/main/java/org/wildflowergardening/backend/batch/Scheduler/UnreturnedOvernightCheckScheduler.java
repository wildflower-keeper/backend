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
public class UnreturnedOvernightCheckScheduler {
    private final JobLauncher jobLauncher;
    private final Job unreturnedOvernightCheckJob;

    @Scheduled(cron = "0 0 23 * * ?")  // 매일 밤 11시에 실행
    public void runLocationStatusCheckJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(unreturnedOvernightCheckJob, jobParameters);
    }

}
