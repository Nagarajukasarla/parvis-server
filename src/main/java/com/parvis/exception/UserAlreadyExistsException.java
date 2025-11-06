package com.parvis.exception;

public class UserAlreadyExistsException extends DatabaseException {
    public UserAlreadyExistsException(String message, String code, String sqlState, Throwable cause) {
        super(message, code, sqlState, cause);
    }
}
