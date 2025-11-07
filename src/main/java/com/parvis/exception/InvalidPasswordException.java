package com.parvis.exception;

import lombok.Getter;

@Getter
public class InvalidPasswordException extends RuntimeException {
    private final String code;

    public InvalidPasswordException(String message, String code) {
        super(message);
        this.code = code;
    }
}
