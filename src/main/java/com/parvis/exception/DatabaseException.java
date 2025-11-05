package com.parvis.exception;

import lombok.Getter;

@Getter
public class DatabaseException extends RuntimeException {
    private final String code;
    private final String sqlState;

    public DatabaseException(String message, String code, String sqlState, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.sqlState = sqlState;
    }
}