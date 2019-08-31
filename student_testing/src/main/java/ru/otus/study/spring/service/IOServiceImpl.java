package ru.otus.study.spring.service;

import java.util.Scanner;

public class IOServiceImpl implements IOService {
    private final Scanner scanner;

    public IOServiceImpl() {
        this.scanner = new Scanner(System.in);
    }


    @Override
    public String getUserInput() {
            return scanner.nextLine();
    }

    @Override
    public void printOutput(String data) {
        System.out.println(data);
    }
}
