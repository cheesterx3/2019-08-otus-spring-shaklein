package ru.otus.study.spring.service;

import java.io.InputStream;
import java.io.PrintStream;

public interface IOProvider {
    PrintStream getOutputStream();
    InputStream getInputStream();
}
