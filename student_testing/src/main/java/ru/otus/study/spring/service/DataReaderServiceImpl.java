package ru.otus.study.spring.service;

import java.util.Scanner;

public class DataReaderServiceImpl implements DataReaderService {
    private final Scanner scanner;

    public DataReaderServiceImpl() {
        this.scanner = new Scanner(System.in);
    }


    @Override
    public String getUserInput() {
            return scanner.nextLine();
    }
}
