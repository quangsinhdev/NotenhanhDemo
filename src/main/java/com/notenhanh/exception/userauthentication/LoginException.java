package com.notenhanh.exception.userauthentication;

import org.springframework.security.core.AuthenticationException;

public class LoginException extends AuthenticationException {
    
    private static final long serialVersionUID = 1L;

    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }
}