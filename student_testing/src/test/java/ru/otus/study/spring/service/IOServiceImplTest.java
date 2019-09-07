package ru.otus.study.spring.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;

class IOServiceImplTest {

    @Test
    void getUserInput() {
        final String dataToWrite = "dataToCheck";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(dataToWrite.getBytes());
        final PrintStream printStream = getPrintStream();
        IOService ioService = new IOServiceImpl(inputStream, printStream);
        Assertions.assertEquals(dataToWrite, ioService.getUserInput());
    }

    @Test
    void printOutput() {
        final MockOutputStringStream outputStringStream=new MockOutputStringStream();
        final PrintStream printStream = new PrintStream(outputStringStream);
        IOService ioService = new IOServiceImpl(new ByteArrayInputStream(new byte[0]), printStream);
        final String dataToWrite = "dataToCheck";
        final String writtenData = dataToWrite + "\r\n";
        ioService.printOutput(dataToWrite);
        Assertions.assertEquals(writtenData, outputStringStream.toString());
    }

    @Test
    void tryToReadIntValuesCorrect() {
        final String dataToWrite = "1 2 3";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(dataToWrite.getBytes());
        final MockOutputStringStream outputStringStream=new MockOutputStringStream();
        final PrintStream printStream = new PrintStream(outputStringStream);
        IOService ioService = new IOServiceImpl(inputStream, printStream);
        List<Integer> correctList = Arrays.asList(1, 2, 3);
        Assertions.assertEquals(correctList, ioService.tryToReadIntValues());
    }

    @Test
    void tryToReadIntValuesInCorrect() {
        final String dataToWrite = "1 2 3";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(dataToWrite.getBytes());
        final MockOutputStringStream outputStringStream=new MockOutputStringStream();
        final PrintStream printStream = new PrintStream(outputStringStream);
        IOService ioService = new IOServiceImpl(inputStream, printStream);
        List<Integer> incorrectList = Arrays.asList(1, 2, 3,4);
        Assertions.assertNotEquals(incorrectList, ioService.tryToReadIntValues());
    }

    @Test
    void tryToReadIntValuesFromEmpty() {
        final String dataToWrite = "\r\n";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(dataToWrite.getBytes());
        final MockOutputStringStream outputStringStream=new MockOutputStringStream();
        final PrintStream printStream = new PrintStream(outputStringStream);
        IOService ioService = new IOServiceImpl(inputStream, printStream);
        Assertions.assertTrue(ioService.tryToReadIntValues().isEmpty());
    }

    private PrintStream getPrintStream() {
        final MockOutputStringStream outputStringStream=new MockOutputStringStream();
        return new PrintStream(outputStringStream);
    }

}