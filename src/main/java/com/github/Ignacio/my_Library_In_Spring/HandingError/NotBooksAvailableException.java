package com.github.Ignacio.my_Library_In_Spring.HandingError;

public class NotBooksAvailableException extends RuntimeException {
    public NotBooksAvailableException(String message) {
        super(message);
    }
}
