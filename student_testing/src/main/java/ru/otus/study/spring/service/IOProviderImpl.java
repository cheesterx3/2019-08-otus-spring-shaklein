package ru.otus.study.spring.service;

import java.io.InputStream;
import java.io.PrintStream;

public class IOProviderImpl implements IOProvider {
    @Override
    public PrintStream getOutputStream() {
        return System.out;
    }

    @Override
    public InputStream getInputStream() {
        return System.in;
    }
}
