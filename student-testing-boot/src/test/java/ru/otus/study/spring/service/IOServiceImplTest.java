package ru.otus.study.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@DisplayName("Методы сервиса взамодействия с вводом/выводом должны ")
@SpringBootTest(classes = {IOServiceImplTest.IOServiceTestConfiguration.class})
class IOServiceImplTest {
    private final static String DATA_TO_TEST = "test";


    @MockBean
    @Qualifier("printBean")
    private PrintStream printStream;

    @Autowired
    @Qualifier("userInputOutputTest")
    private IOService serviceInputOutput;
    @Autowired
    @Qualifier("tryToReadIntValuesFromCorrectLine")
    private IOService serviceInputForReadCorrectInt;
    @Autowired
    @Qualifier("tryToReadIntValuesFromEmptyLine")
    private IOService serviceInputForReadIntFromEmptyLine;
    @Autowired
    @Qualifier("tryToReadIntValuesFromIncorrectLine")
    private IOService serviceInputForReadIntFromIncorrectLine;

    @TestConfiguration
    static class IOServiceTestConfiguration {
        @Bean
        @Qualifier("printBean")
        public PrintStream getPrintStream() {
            return new PrintStream(new ByteArrayOutputStream());
        }

        @Bean
        @Qualifier("userInputOutputTest")
        public IOService createIOService(PrintStream printStream) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(DATA_TO_TEST.getBytes());
            return new IOServiceImpl(inputStream, printStream);
        }

        @Bean
        @Qualifier("tryToReadIntValuesFromCorrectLine")
        public IOService createIOServiceForIntParse() {
            ByteArrayInputStream inputStream = new ByteArrayInputStream("1 2 3 4".getBytes());
            return new IOServiceImpl(inputStream, new PrintStream(new ByteArrayOutputStream()));
        }

        @Bean
        @Qualifier("tryToReadIntValuesFromEmptyLine")
        public IOService createIOServiceForIntParseFromEmpty() {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(" ".getBytes());
            return new IOServiceImpl(inputStream, new PrintStream(new ByteArrayOutputStream()));
        }

        @Bean
        @Qualifier("tryToReadIntValuesFromIncorrectLine")
        public IOService createIOServiceForIntParseFromIncorrect() {
            ByteArrayInputStream inputStream = new ByteArrayInputStream("some non integer data".getBytes());
            return new IOServiceImpl(inputStream, new PrintStream(new ByteArrayOutputStream()));
        }
    }

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("считывать данные при вводе пользователем строки. Метод getUserInput")
    void getUserInput() {
        assertEquals(DATA_TO_TEST, serviceInputOutput.getUserInput());
    }

    @Test
    @DisplayName("выводить сообщения пользователю. Метод printOutput")
    void printOutput() {
        serviceInputOutput.printOutput(DATA_TO_TEST);
        verify(printStream, times(1)).println(DATA_TO_TEST);
    }

    @Test
    @DisplayName("формировать список целочисленных значений при вводе чисел через пробел. Метод tryToReadIntValues")
    void tryToReadIntValuesFromCorrectLine() {
        final List<Integer> integerList = serviceInputForReadCorrectInt.tryToReadIntValues();
        assertTrue(integerList.containsAll(Arrays.asList(1, 2, 3, 4)));
    }

    @Test
    @DisplayName("формировать пустой список целочиселнных значений при вводе пустой строки. Метод tryToReadIntValues")
    void tryToReadIntValuesFromEmpty() {
        final List<Integer> integerList = serviceInputForReadIntFromEmptyLine.tryToReadIntValues();
        assertTrue(integerList.isEmpty());
    }

    @Test
    @DisplayName("формировать пустой список целочиселнных значений при вводе строки, не содержащй числа. Метод tryToReadIntValues")
    void tryToReadIntValuesFromNonIntegerLine() {
        final List<Integer> integerList = serviceInputForReadIntFromIncorrectLine.tryToReadIntValues();
        assertTrue(integerList.isEmpty());
    }
}