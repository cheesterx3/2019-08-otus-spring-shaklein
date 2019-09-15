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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@DisplayName("Методы сервиса запроса имени студента ")
@SpringBootTest(classes = StudentInfoAskServiceImpl.class)
class StudentInfoAskServiceImplTest {
    private final static String USER_NAME="Иванов";
    private final static String GREETING="Введите имя";

    @MockBean
    private IOService ioService;

    @MockBean
    @Qualifier("messageLocalizationService")
    private LocalizationService localizationService;

    @Autowired
    private StudentInfoAskService service;

    @BeforeEach
    void setUp() {
        given(this.ioService.getUserInput()).willReturn(USER_NAME);
        given(this.localizationService.getLocalized(StudentInfoAskServiceImpl.ASK_NAME_MSG)).willReturn(GREETING);
    }

    @Test
    @DisplayName("выдать запрос на ввод имени пользователя. ")
    void askUserToInputName() {
        service.getStudentInfo();
        verify(ioService,times(1)).printOutput(GREETING);
    }

    @Test
    @DisplayName("создать not null экземпляр класса StudentNameInfo")
    void createStudentInfoIsNotNull() {
        final StudentNameInfo studentNameInfo=service.getStudentInfo();
        assertNotNull(studentNameInfo);
    }

    @Test
    @DisplayName("создать экземпляр класса StudentNameInfo по введённым пользователем данным.")
    void createStudentInfo() {
        final StudentNameInfo studentNameInfo=service.getStudentInfo();
        assertEquals(studentNameInfo.getName(),USER_NAME);
    }
}