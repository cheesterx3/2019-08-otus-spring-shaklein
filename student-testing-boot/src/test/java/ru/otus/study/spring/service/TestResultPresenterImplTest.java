package ru.otus.study.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.study.spring.domain.StudentNameInfo;
import ru.otus.study.spring.service.i18n.LocalizationService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Методы сервиса вывода результата тестирования ")
@SpringBootTest(classes = TestResultPresenterImpl.class)
class TestResultPresenterImplTest {
    private final static String APPEAL_MSG = "message.appeal";
    private final static String APPEAL_MSG_SHOW = "DEAR USER";
    private final static String TEST_RESULTS = "results of test";
    private final static String STUD_NAME = "Student";

    private final StudentNameInfo nameInfo = new StudentNameInfo(STUD_NAME);

    @MockBean
    private IOService ioService;
    @MockBean
    @Qualifier("messageLocalizationService")
    private LocalizationService localizationService;
    @Autowired
    private TestResultPresenter service;

    @BeforeEach
    void setUp() {
        given(localizationService.getLocalized(APPEAL_MSG, new String[]{STUD_NAME})).willReturn(APPEAL_MSG_SHOW);
    }

    @Test
    @DisplayName("выдать приветствие пользователю и результаты тестирования. Метод showResults")
    void showResults() {
        service.showResults(nameInfo, TEST_RESULTS);
        verify(ioService, times(1)).printOutput(APPEAL_MSG_SHOW);
        verify(ioService, times(1)).printOutput(TEST_RESULTS);
    }
}
