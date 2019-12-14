package ru.otus.study.spring.integrationtask.helper;

import static java.util.Objects.nonNull;

public final class ValidationUtils {
    public static boolean isDataValid(String message) {
        return nonNull(message) && message.matches("\\{(\"sensorId\"):.*,(\"type\"):.*,(\"message\"):.*}");
    }
}
