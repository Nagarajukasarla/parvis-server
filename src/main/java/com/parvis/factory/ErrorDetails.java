package com.parvis.factory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.parvis.enums.ErrorOrigin;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorDetails(
        ErrorOrigin origin,
        String message,
        String code,
        String sqlState,
        String hint,

        @JsonIgnore
        Throwable cause
) {

    public static ErrorDetails of(ErrorOrigin origin, String message, String code) {
        return new ErrorDetails(origin, message, code, null, null, null);
    }

    public static ErrorDetails db(String message, String code, String sqlState, String hint, Throwable cause) {
        return new ErrorDetails(ErrorOrigin.DATABASE, message, code, sqlState, hint, cause);
    }

    public static ErrorDetails repository(String message, String code, Throwable cause) {
        return new ErrorDetails(ErrorOrigin.REPOSITORY, message, code, null, null, cause);
    }

    public static ErrorDetails service(String message, String code, Throwable cause) {
        return new ErrorDetails(ErrorOrigin.SERVICE, message, code, null, null, cause);
    }

    public static ErrorDetails controller(String message, String code, Throwable cause) {
        return new ErrorDetails(ErrorOrigin.CONTROLLER, message, code, null, null, cause);
    }
}
