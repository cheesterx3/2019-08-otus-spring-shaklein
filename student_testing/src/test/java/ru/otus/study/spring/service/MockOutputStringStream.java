package ru.otus.study.spring.service;

import java.io.OutputStream;

public class MockOutputStringStream extends OutputStream {
    private StringBuilder outputString = new StringBuilder();

    @Override
    public void write(int x)  {
        outputString.append((char) x);
    }

    public String toString() {
        return outputString.toString();
    }
}
