package com.example.urlShorter.urls.exceptions;

public class ClickLimitExceededException extends RuntimeException {
    public ClickLimitExceededException(String message) {
        super(message);
    }
}
