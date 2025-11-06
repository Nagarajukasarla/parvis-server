package com.parvis.exception;

import lombok.Getter;

@Getter
public class InvalidRequestException extends RuntimeException {
    private final String code;

    public InvalidRequestException(String message, String code) {
        super(message);
        this.code = code;
    }
}