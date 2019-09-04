package ru.otus.study.spring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IOServiceImpl implements IOService {
    private final Scanner scanner;
    private final IOProvider ioProvider;

    public IOServiceImpl(IOProvider ioProvider) {
        this.ioProvider = ioProvider;
        this.scanner = new Scanner(ioProvider.getInputStream());
    }


    @Override
    public String getUserInput() {
        return scanner.nextLine();
    }

    @Override
    public void printOutput(String data) {
        ioProvider.getOutputStream().println(data);
    }

    @Override
    public List<Integer> tryToReadIntValues() {
        final Scanner parseScanner = new Scanner(getUserInput());
        final List<Integer> indexList = new ArrayList<>();
        try {
            if (parseScanner.hasNextInt()) {
                indexList.add(parseScanner.nextInt());
                while (parseScanner.hasNextInt()) {
                    indexList.add(parseScanner.nextInt());
                }
            }
        } finally {
            parseScanner.close();
        }
        return indexList;

    }
}
