package ru.otus.study.spring.service;

import ru.otus.study.spring.domain.StudentNameInfo;

public class StudentInfoAskServiceImpl implements StudentInfoAskService {
    private final DataReaderService dataReaderService;

    public StudentInfoAskServiceImpl(DataReaderService dataReaderService) {
        this.dataReaderService = dataReaderService;
    }

    @Override
    public StudentNameInfo getStudentInfo() {
        return new StudentNameInfo(tryReadStudentName());
    }

    private String tryReadStudentName() {
        System.out.println("Введите своё имя, пожалуйста.");
        String name;
        if ((name = dataReaderService.getUserInput()) != null) {
            return name.trim().isEmpty() ? tryReadStudentName() : name;
        } else {
            return tryReadStudentName();
        }

    }
}
