package ru.otus.study.spring.batch.librarymigration.shell;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.study.spring.batch.librarymigration.service.JobManageService;

@ShellComponent
@RequiredArgsConstructor
public class ShellAppStarter {
    private final JobManageService jobManageService;

    @ShellMethod(value = "Job start command", key = {"start"})
    @ShellMethodAvailability(value = "isAvailableToStart")
    public void start() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        jobManageService.startJob();
    }

    private Availability isAvailableToStart() {
        return jobManageService.isAvailableToStart() ? Availability.available() : Availability.unavailable("Job is completed. Call \"restart\" to restart job");
    }

    @ShellMethod(value = "Job restart command", key = {"restart"})
    @ShellMethodAvailability(value = "isAvailableToReStart")
    public void restart() throws JobParametersInvalidException, JobRestartException, JobInstanceAlreadyCompleteException, NoSuchJobExecutionException,
            NoSuchJobException, JobParametersNotFoundException, JobExecutionAlreadyRunningException {
        jobManageService.restartJob();
    }

    private Availability isAvailableToReStart() {
        return jobManageService.isAvailableToReStart() ? Availability.available() : Availability.unavailable("Job was not started. Call \"start\" to start job first");
    }
}
