package com.parvis.dto;

import com.parvis.enums.Shift;

import java.util.Optional;

public record EmployeeCreateRequest(
        Optional<String> empId,
        Optional<String> name,
        Optional<String> email,
        Optional<String> password,
        Optional<Shift> shift
) {}