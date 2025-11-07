package com.parvis.dto;

import com.parvis.enums.Shift;
import lombok.Builder;

@Builder
public record EmployeeUpdateResponse(
        String empId,
        String email,
        String name,
        Shift shift
) {}
