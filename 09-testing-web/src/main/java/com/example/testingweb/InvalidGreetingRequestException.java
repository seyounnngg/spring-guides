package com.example.testingweb;

public class InvalidGreetingRequestException extends RuntimeException {

    public InvalidGreetingRequestException(String message) {
        super(message);
    }
}
