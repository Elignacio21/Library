package com.github.Ignacio.my_Library_In_Spring.HandingError;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
