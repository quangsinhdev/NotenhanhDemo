package com.notenhanh.exception.userauthentication;

public class RecoveryException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public RecoveryException(String message) {
        super(message);
    }

    public RecoveryException(String message, Throwable cause) {
        super(message, cause);
    }
}