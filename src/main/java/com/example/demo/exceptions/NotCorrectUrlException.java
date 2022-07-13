package com.example.demo.exceptions;

public class NotCorrectUrlException extends RuntimeException{
    public NotCorrectUrlException(String message) {
        super(message);
    }
}
