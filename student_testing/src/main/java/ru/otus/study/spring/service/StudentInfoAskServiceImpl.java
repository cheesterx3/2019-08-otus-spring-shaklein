package ru.otus.study.spring.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.otus.study.spring.domain.StudentNameInfo;
import ru.otus.study.spring.service.i18n.LocalizationService;

import java.util.Objects;

@Service
public class StudentInfoAskServiceImpl implements StudentInfoAskService {
    private final static String ASK_NAME_MSG = "message.username";
    private final IOService ioService;
    private final LocalizationService localizationService;

    public StudentInfoAskServiceImpl(IOService ioService,
                                     @Qualifier("messageLocalizationService") LocalizationService localizationService) {
        this.ioService = ioService;
        this.localizationService = localizationService;
    }

    @Override
    public StudentNameInfo getStudentInfo() {
        return new StudentNameInfo(tryReadStudentName());
    }

    private String tryReadStudentName() {
        askStudentName();
        String name = ioService.getUserInput();
        if (isNameCorrect(name)) {
            return name;
        }
        return tryReadStudentName();
    }

    private void askStudentName() {
        ioService.printOutput(localizationService.getLocalized(ASK_NAME_MSG));
    }

    private boolean isNameCorrect(String name) {
        return Objects.nonNull(name) && !name.trim().isEmpty();
    }
}
