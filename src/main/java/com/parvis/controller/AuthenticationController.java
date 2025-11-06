package com.parvis.controller;

import com.parvis.dto.EmployeeLoginRequest;
import com.parvis.enums.ErrorOrigin;
import com.parvis.exception.InvalidPasswordException;
import com.parvis.exception.UserNotFoundException;
import com.parvis.factory.AppResponse;
import com.parvis.factory.ErrorDetails;
import com.parvis.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AppResponse<?>> login(@RequestBody EmployeeLoginRequest request, HttpServletRequest httpRequest) {
        var result = authenticationService.validateUser(request);
        if (result.success()) {
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute("user", String.valueOf(result.data()));
            return ResponseEntity.ok(AppResponse.success("Login successful"));
        }
        else {
            ErrorDetails error = result.errorDetails();
            if (error.origin() == ErrorOrigin.DATABASE) {
                if (error.cause() instanceof InvalidPasswordException) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
                }
                if (error.cause() instanceof UserNotFoundException) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
                }
            }
            if (error.origin() == ErrorOrigin.SERVICE) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
