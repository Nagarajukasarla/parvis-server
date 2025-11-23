package com.parvis.exception;

public final class UserAlreadyExistsException extends DatabaseException {
    public UserAlreadyExistsException(String message, String code, String sqlState, String hint, Throwable cause) {
        super(message, code, sqlState, hint, cause);
    }
}
