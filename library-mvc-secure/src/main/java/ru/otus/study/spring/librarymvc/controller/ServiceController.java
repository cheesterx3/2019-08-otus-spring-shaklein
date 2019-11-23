package ru.otus.study.spring.librarymvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ServiceController {

    @GetMapping("/denied")
    public String deniedPage() {
        return "err403";
    }
}
