package com.example.pass.job.pass;

import com.example.pass.config.TestBatchConfig;
import com.example.pass.repository.pass.PassEntity;
import com.example.pass.repository.pass.PassRepository;
import com.example.pass.repository.pass.PassStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import javax.batch.runtime.JobExecution;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@SpringBatchTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {ExpirePassesJobConfiguration.class, TestBatchConfig.class})
public class ExpirePassesJobConfigurationTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private PassRepository passRepository;

    @Test
    public void test_expirePassesStep() throws Exception {
        // given
        addPassEntities(10);

        // when
        JobExecution jobExecution = (JobExecution) jobLauncherTestUtils.launchJob();
        String jobName = jobExecution.getJobName();

        // then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        assertEquals("expiredPassesJob", jobName);
    }

    private void addPassEntities(int size) {
        final LocalDateTime now = LocalDateTime.now();
        final Random random = new Random();

        List<PassEntity> passEntities = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            PassEntity passEntity = new PassEntity();
            passEntity.setPackageSeq(1);
            passEntity.setUserId("A" + 1000000 + i);
            passEntity.setStatus(PassStatus.PROGRESSED);
            passEntity.setRemainingCount(random.nextInt(11));
            passEntity.setStartedAt(now.minusDays(60));
            passEntity.setEndedAt(now.minusDays(1));
            passEntities.add(passEntity);
        }
        passRepository.saveAll(passEntities);
    }
}
