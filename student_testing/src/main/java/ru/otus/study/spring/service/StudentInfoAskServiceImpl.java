package ru.otus.study.spring.service;

import ru.otus.study.spring.domain.StudentNameInfo;

public class StudentInfoAskServiceImpl implements StudentInfoAskService {
    private final IOService ioService;

    public StudentInfoAskServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public StudentNameInfo getStudentInfo() {
        return new StudentNameInfo(tryReadStudentName());
    }

    private String tryReadStudentName() {
        ioService.printOutput("Введите своё имя, пожалуйста.");
        String name;
        if ((name = ioService.getUserInput()) != null) {
            return name.trim().isEmpty() ? tryReadStudentName() : name;
        } else {
            return tryReadStudentName();
        }

    }
}
