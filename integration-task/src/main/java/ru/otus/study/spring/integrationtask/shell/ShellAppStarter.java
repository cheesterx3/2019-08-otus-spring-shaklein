package ru.otus.study.spring.integrationtask.shell;


import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.study.spring.integrationtask.exceptions.AlreadyStartedException;
import ru.otus.study.spring.integrationtask.service.AppStarterService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@ShellComponent
@RequiredArgsConstructor
public class ShellAppStarter {
    private final AppStarterService appStarterService;
    private boolean isStarted = false;

    @ShellMethod(value = "App start command", key = {"start"})
    @ShellMethodAvailability(value = "isAvailableToStart")
    public void start(@Min(1) @Max(100) @ShellOption(defaultValue = "1") int threadCount) throws AlreadyStartedException {
        appStarterService.start(threadCount);
        isStarted = true;
    }

    private Availability isAvailableToStart() {
        return isStarted ? Availability.unavailable("App flow is already started") : Availability.available();
    }

    @ShellMethod(value = "App stop command", key = {"stop"})
    public void stop() {
        appStarterService.stop();
        isStarted=false;
    }

}
