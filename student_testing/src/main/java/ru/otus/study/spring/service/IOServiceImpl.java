package ru.otus.study.spring.service;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class IOServiceImpl implements IOService {
    private final Scanner scanner;
    private final PrintStream printStream;


    public IOServiceImpl(InputStream inputStream, PrintStream printStream) {
        this.printStream = printStream;
        this.scanner = new Scanner(inputStream);
    }

    @Override
    public String getUserInput() {
        return scanner.nextLine();
    }

    @Override
    public void printOutput(String data) {
        printStream.println(data);
    }

    @Override
    public List<Integer> tryToReadIntValues() {
        final Scanner parseScanner = new Scanner(getUserInput().trim());
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
