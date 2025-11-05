package com.parvis.response;

import lombok.Builder;

@Builder
public record EmployeeCreateResponse(
    Integer id,
    String empId
) {}
