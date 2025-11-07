package com.parvis.controller;

import com.parvis.dto.EmployeeCreateRequest;
import com.parvis.enums.ErrorOrigin;
import com.parvis.factory.AppResponse;
import com.parvis.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/create-employee")
    public ResponseEntity<AppResponse<?>> createEmployee(@RequestBody EmployeeCreateRequest request) {
        var result = employeeService.createEmployee(request);
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

    @PatchMapping("/update-employee")
    public ResponseEntity<AppResponse<?>> updateEmployee(@RequestBody EmployeeCreateRequest request) {
        var result = employeeService.updateEmployee(request);
        if (result.success()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}
