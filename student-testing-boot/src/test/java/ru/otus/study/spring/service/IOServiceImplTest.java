package ru.otus.study.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("Методы сервиса взамодействия с вводом/выводом должны ")
@ExtendWith(MockitoExtension.class)
class IOServiceImplTest {
    private final static String DATA_TO_TEST = "test";

    private IOService service;
    private ByteArrayInputStream inputStream;
    private PrintStream printStream;
    private ByteArrayOutputStream outStream;

    @BeforeEach
    void setUp() {
        outStream = new ByteArrayOutputStream();
        printStream = new PrintStream(outStream);
    }

    @Test
    @DisplayName("считывать данные при вводе пользователем строки. Метод getUserInput")
    void getUserInput() {
        inputStream = new ByteArrayInputStream(DATA_TO_TEST.getBytes());
        service = new IOServiceImpl(inputStream, printStream);
        assertEquals(DATA_TO_TEST, service.getUserInput());
    }

    @Test
    @DisplayName("выводить сообщения пользователю. Метод printOutput")
    void printOutput() {
        final String expected = DATA_TO_TEST.concat("\r\n");
        inputStream = new ByteArrayInputStream(new byte[0]);
        service = new IOServiceImpl(inputStream, printStream);
        service.printOutput(DATA_TO_TEST);
        assertEquals(new String(outStream.toByteArray()), expected);
    }

    @Test
    @DisplayName("формировать список целочисленных значений при вводе чисел через пробел. Метод tryToReadIntValues")
    void tryToReadIntValuesFromCorrectLine() {
        final String lineToTest = "1 2 3 4";
        inputStream = new ByteArrayInputStream(lineToTest.getBytes());
        service = new IOServiceImpl(inputStream, printStream);
        final List<Integer> integerList = service.tryToReadIntValues();
        assertTrue(integerList.containsAll(Arrays.asList(1, 2, 3, 4)));
    }

    @Test
    @DisplayName("формировать пустой список целочиселнных значений при вводе пустой строки. Метод tryToReadIntValues")
    void tryToReadIntValuesFromEmpty() {
        final String lineToTest = " ";
        inputStream = new ByteArrayInputStream(lineToTest.getBytes());
        service = new IOServiceImpl(inputStream, printStream);
        final List<Integer> integerList = service.tryToReadIntValues();
        assertTrue(integerList.isEmpty());
    }

    @Test
    @DisplayName("формировать пустой список целочиселнных значений при вводе строки, не содержащй числа. Метод tryToReadIntValues")
    void tryToReadIntValuesFromNonIntegerLine() {
        final String lineToTest = "some input data";
        inputStream = new ByteArrayInputStream(lineToTest.getBytes());
        service = new IOServiceImpl(inputStream, printStream);
        final List<Integer> integerList = service.tryToReadIntValues();
        assertTrue(integerList.isEmpty());
    }
}