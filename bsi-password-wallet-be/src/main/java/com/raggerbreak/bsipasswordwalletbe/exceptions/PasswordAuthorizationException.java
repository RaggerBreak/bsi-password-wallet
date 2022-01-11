package com.raggerbreak.bsipasswordwalletbe.exceptions;

public class PasswordAuthorizationException extends RuntimeException {

    public PasswordAuthorizationException() {
    }

    public PasswordAuthorizationException(String message) {
        super(message);
    }
}
