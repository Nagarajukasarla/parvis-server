package com.parvis.exception;

public class ShiftNotFoundException extends DatabaseException {
    public ShiftNotFoundException(String message, String code, String sqlState, String hint, Throwable cause) {
        super(message, code, sqlState, hint, cause);
    }
}
