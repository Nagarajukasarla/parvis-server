package com.parvis.response;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record EmployeeResponse (
    Integer id,
    String empId,
    String name,
    OffsetDateTime[] shiftTimings,
    OffsetDateTime[] totalDays,
    OffsetDateTime[] attended,
    OffsetDateTime[] leaves
) {}
