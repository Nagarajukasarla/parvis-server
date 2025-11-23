package com.parvis.controller;

import com.parvis.dto.EmployeeAttendanceRequest;
import com.parvis.enums.ErrorOrigin;
import com.parvis.factory.AppResponse;
import com.parvis.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/me")
    public ResponseEntity<String> authenticate(HttpSession session) {
        String empId = (String) session.getAttribute("user");
        if (empId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Not logged in");
        }
        return ResponseEntity.ok("secure");
    }

    @GetMapping("/profile")
    public ResponseEntity<AppResponse<?>> getEmployeeProfile(HttpSession session) {
        String empId = (String) session.getAttribute("user");
        var result = employeeService.getEmployeeProfile(empId);
        if (result.success()) {
            return ResponseEntity.ok(result);
        }
        else if (result.errorDetails().origin() == ErrorOrigin.DATABASE) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PostMapping(value = "/mark", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AppResponse<?>> markEmployeeAttendance(
            @ModelAttribute EmployeeAttendanceRequest request,
            HttpSession session
    ) {
        String empId = (String) session.getAttribute("user");

        var result = employeeService.markAttendance(empId, request);

        if (result.success()) {
            return ResponseEntity.ok(result);
        }
        else if (result.errorDetails().origin() == ErrorOrigin.DATABASE) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/preferences")
    public ResponseEntity<AppResponse<?>> getEmployeePreferences() {
        var result = employeeService.getEmployeePreferences();
        if (result.success()) {
            return ResponseEntity.ok(result);
        }
        else if (result.errorDetails().origin() == ErrorOrigin.DATABASE) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/marked-status")
    public ResponseEntity<AppResponse<?>> getEmployeeAttendanceStatus(HttpSession session) {
        String empId = (String) session.getAttribute("user");

        var result = employeeService.hasMarkedInToday(empId);
        if (result.success()) {
            return ResponseEntity.ok(result);
        }
        else if (result.errorDetails().origin() == ErrorOrigin.DATABASE) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

}
