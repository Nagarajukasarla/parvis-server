package com.parvis.controller;

import com.parvis.dto.EmployeeAttendanceRequest;
import com.parvis.enums.ErrorOrigin;
import com.parvis.factory.AppResponse;
import com.parvis.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

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

//    @PostMapping("/mark")
//    public ResponseEntity<AppResponse<?>> markEmployeeAttendance(@RequestBody EmployeeAttendanceRequest request) {
//        var result = employeeService.markAttendance(request);
//        if (result.success()) {
//            return ResponseEntity.ok(result);
//        }
//        else if (result.errorDetails().origin() == ErrorOrigin.DATABASE) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
//        }
//        else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
//        }
//    }

}
