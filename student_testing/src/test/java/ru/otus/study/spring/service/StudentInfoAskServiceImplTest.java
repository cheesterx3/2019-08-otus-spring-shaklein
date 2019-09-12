package ru.otus.study.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.study.spring.domain.StudentNameInfo;
import ru.otus.study.spring.service.i18n.LocalizationService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Методы сервиса запроса имени студенты ")
@ExtendWith(MockitoExtension.class)
class StudentInfoAskServiceImplTest {
    private final static String USER_NAME="Иванов";
    private final static String GREETING="Введите имя";

    @Mock
    private IOService ioService;
    @Mock
    private LocalizationService localizationService;

    private StudentInfoAskService service;

    @BeforeEach
    void setUp() {
        given(ioService.getUserInput()).willReturn(USER_NAME);
        given(localizationService.getLocalized(StudentInfoAskServiceImpl.ASK_NAME_MSG)).willReturn(GREETING);
        service = new StudentInfoAskServiceImpl(ioService, localizationService);
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