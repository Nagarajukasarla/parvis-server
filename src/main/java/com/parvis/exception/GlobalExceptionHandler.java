package com.parvis.exception;

import com.parvis.factory.AppResponse;
import com.parvis.factory.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

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
                                ex.getHint(),
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
}
