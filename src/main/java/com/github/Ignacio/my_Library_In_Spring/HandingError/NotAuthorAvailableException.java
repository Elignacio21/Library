package com.github.Ignacio.my_Library_In_Spring.HandingError;

public class NotAuthorAvailableException extends RuntimeException {
    public NotAuthorAvailableException(String message) {
        super(message);
    }
}
