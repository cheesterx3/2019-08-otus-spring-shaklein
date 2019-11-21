package ru.otus.study.spring.librarymvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.study.spring.librarymvc.exception.DaoException;
import ru.otus.study.spring.librarymvc.exception.NotFoundException;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {
    @ExceptionHandler(DaoException.class)
    public ModelAndView handleDaoException(DaoException e) {
        ModelAndView modelAndView = new ModelAndView("err500");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(NotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("err404");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }
}
