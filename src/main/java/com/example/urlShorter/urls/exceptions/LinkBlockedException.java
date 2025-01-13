package com.example.urlShorter.urls.exceptions;

public class LinkBlockedException extends RuntimeException {
    public LinkBlockedException(String message) {
        super(message);
    }
}