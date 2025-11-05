package com.parvis.exception;

import com.parvis.factory.AppResponse;
import com.parvis.factory.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<AppResponse<?>> handleDatabaseException(DatabaseException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(AppResponse.failure(
                        ErrorDetails.db(
                                ex.getMessage(),
                                ex.getCode(),
                                ex.getSqlState(),
                                ex
                        )
                ));
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<AppResponse<?>> handleInvalidRequest(InvalidRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(AppResponse.failure(
                        ErrorDetails.service(
                                ex.getMessage(),
                                ex.getCode(),
                                ex
                        )
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppResponse<?>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(AppResponse.failure(
                        ErrorDetails.repository(
                                "An unexpected error occurred.",
                                "UNKNOWN_ERROR",
                                ex
                        )
                ));
    }
}
