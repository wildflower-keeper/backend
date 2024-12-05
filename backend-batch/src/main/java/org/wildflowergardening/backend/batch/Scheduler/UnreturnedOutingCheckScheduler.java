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
public class UnreturnedOutingCheckScheduler {
    private final JobLauncher jobLauncher;
    private final Job unreturnedOutingCheckJob;

    @Scheduled(cron = "0 0 7 * * ?")  // 매일 아침 7시에 실행
    public void runLocationStatusCheckJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(unreturnedOutingCheckJob, jobParameters);
    }

}
