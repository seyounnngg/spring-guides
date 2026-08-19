package com.example.testingweb;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GreetingExceptionHandler {

    @ExceptionHandler(InvalidGreetingRequestException.class)
    public ResponseEntity<String> handleInvalidGreeting(InvalidGreetingRequestException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
