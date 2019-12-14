package ru.otus.study.spring.integrationtask.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.study.spring.integrationtask.domain.MessageType;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.study.spring.integrationtask.helper.ValidationUtils.isDataValid;

@DisplayName("Валидатор сообщений от сенсоров ")
class ValidationUtilsTest {

    @Test
    @DisplayName("должен возвращать true при корректном сообщении ")
    void shouldReturnTrueIfCommandIsCorrect(){
        final String message = String.format("{\"sensorId\":\"%s\",\"type\":\"%s\",\"message\":\"MESSAGE\"}", "001", MessageType.Alarm.name());
        assertThat(isDataValid(message)).isTrue();
    }

    @Test
    @DisplayName("должен возвращать false при некорректном сообщении ")
    void shouldReturnFalseIfCommandIsInCorrect(){
        final String message = String.format("{\"someField\":\"%s\",\"type\":\"%s\",\"message\":\"MESSAGE\"}", "001", MessageType.Alarm.name());
        assertThat(isDataValid(message)).isFalse();
    }
}