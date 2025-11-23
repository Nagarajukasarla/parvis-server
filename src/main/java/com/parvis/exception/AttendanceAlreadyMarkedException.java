package com.parvis.exception;

public final class AttendanceAlreadyMarkedException extends DatabaseException {
    public AttendanceAlreadyMarkedException(String message, String code, String sqlState, String hint, Throwable cause) {
        super(message, code, sqlState, hint, cause);
    }
}
