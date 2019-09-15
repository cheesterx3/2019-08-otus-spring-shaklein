package ru.otus.study.spring.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.otus.study.spring.domain.StudentNameInfo;
import ru.otus.study.spring.service.i18n.LocalizationService;

@Service
public class TestResultPresenterImpl implements TestResultPresenter {
    private final static String APPEAL_MSG = "message.appeal";

    private final IOService ioService;
    private final LocalizationService localizationService;

    public TestResultPresenterImpl(IOService ioService,
                                   @Qualifier("messageLocalizationService") LocalizationService localizationService) {
        this.ioService = ioService;
        this.localizationService = localizationService;
    }

    @Override
    public void showResults(StudentNameInfo studentNameInfo, String testResults) {
        ioService.printOutput(getAppealMessage(studentNameInfo));
        ioService.printOutput(testResults);
    }

    private String getAppealMessage(StudentNameInfo studentNameInfo) {
        return localizationService.getLocalized(APPEAL_MSG, new String[]{studentNameInfo.getName()});
    }
}
