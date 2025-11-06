package com.parvis.exception;

import lombok.Getter;

@Getter
public class DatabaseException extends RuntimeException {
    private final String code;
    private final String sqlState;
    private final String hint;

    public DatabaseException(String message, String code, String sqlState, String hint, Throwable cause) {
        super(message, cause, false, false);
        this.code = code;
        this.sqlState = sqlState;
        this.hint = hint;
    }
}
