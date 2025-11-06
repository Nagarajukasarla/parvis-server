package com.parvis.exception;

public class InvalidPasswordException extends DatabaseException {
    public InvalidPasswordException(String message, String code, String sqlState, String hint, Throwable cause) {
        super(message, code, sqlState, hint, cause);
    }
}
