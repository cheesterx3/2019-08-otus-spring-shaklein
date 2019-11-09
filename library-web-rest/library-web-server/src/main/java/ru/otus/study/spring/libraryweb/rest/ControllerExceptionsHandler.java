package ru.otus.study.spring.libraryweb.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.study.spring.libraryweb.exception.DaoException;
import ru.otus.study.spring.libraryweb.exception.NotFoundException;

@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionsHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
    }

    @ExceptionHandler(DaoException.class)
    public ResponseEntity<String> handleDaoException(DaoException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
    }

}
