package com.parvis.request;

import com.parvis.enums.Shift;

public record EmployeeCreateRequest(
    String name,
    String email,
    String password,
    Shift shift
) {}