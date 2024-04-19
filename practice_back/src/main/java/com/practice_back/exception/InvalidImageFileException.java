package com.practice_back.exception;


public class InvalidImageFileException extends RuntimeException {
    public InvalidImageFileException(String message) {
        super(message);
    }
}