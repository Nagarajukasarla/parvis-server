package com.parvis.exception;

public final class NoOpenPunchExistsException extends DatabaseException {
    public NoOpenPunchExistsException(String message, String code, String sqlState, String hint, Throwable cause) {
        super(message, code, sqlState, hint, cause);
    }
}
