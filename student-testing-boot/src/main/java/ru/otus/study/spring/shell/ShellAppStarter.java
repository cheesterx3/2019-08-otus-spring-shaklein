package ru.otus.study.spring.shell;


import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.study.spring.domain.StudentNameInfo;
import ru.otus.study.spring.service.AppGreeterService;
import ru.otus.study.spring.service.AppRunner;
import ru.otus.study.spring.service.IOService;
import ru.otus.study.spring.service.TestingService;
import ru.otus.study.spring.service.i18n.LocalizationService;

@ShellComponent
public class ShellAppStarter implements AppGreeterService {
    private final AppRunner appRunner;
    private final IOService ioService;
    private final LocalizationService localizationService;
    private boolean isTestFinished;

    public ShellAppStarter(AppRunner appRunner, IOService ioService,
                           LocalizationService localizationService,
                           TestingService testingService) {
        this.appRunner = appRunner;
        this.ioService = ioService;
        this.localizationService = localizationService;
    }

    @ShellMethod(value = "App start command", key = {"start"})
    @ShellMethodAvailability(value = "isAvailableToStart")
    public void start(@ShellOption(defaultValue = "") String name) {
        if (!name.isEmpty()) {
            appRunner.setStudentNameAndRun(new StudentNameInfo(name));
        } else {
            appRunner.run();
        }
        isTestFinished = true;
    }

    @Override
    public void appGreet() {
        ioService.printOutput(localizationService.getLocalized("message.appstart"));
    }

    private Availability isAvailableToStart() {
        return isTestFinished ? Availability.unavailable(localizationService.getLocalized("message.finished")) : Availability.available();
    }
}
