package com.parvis.exception;

public final class UserNotFoundException extends DatabaseException {
    public UserNotFoundException(String message, String code, String sqlState, String hint, Throwable cause) {
        super(message, code, sqlState, hint, cause);
    }
}
