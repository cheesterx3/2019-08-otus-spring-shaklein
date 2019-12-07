package ru.otus.study.spring.batch.librarymigration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultJobManageService implements JobManageService {
    private final Job job;
    private final JobLauncher jobLauncher;
    private final JobOperator jobOperator;

    private JobExecution jobExecution;

    @Override
    public void startJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        jobExecution = jobLauncher.run(job, new JobParametersBuilder().toJobParameters());
    }

    public void restartJob() throws JobParametersInvalidException, JobRestartException, JobInstanceAlreadyCompleteException,
            NoSuchJobExecutionException, NoSuchJobException {
        if (jobExecution != null) {
            jobOperator.restart(jobExecution.getJobId());
        }
    }

    @Override
    public boolean isAvailableToStart() {
        return jobExecution == null;
    }

    @Override
    public boolean isAvailableToReStart() {
        return jobExecution != null;
    }

}
