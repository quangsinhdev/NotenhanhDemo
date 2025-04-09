package com.notenhanh.exception.userauthentication;

public class RegisterException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public RegisterException(String message) {
        super(message);
    }

    public RegisterException(String message, Throwable cause) {
        super(message, cause);
    }
}