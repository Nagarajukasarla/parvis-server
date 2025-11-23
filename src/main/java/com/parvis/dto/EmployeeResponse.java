package com.parvis.dto;

import com.parvis.model.Shift;
import lombok.Builder;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Builder
public record EmployeeResponse (
    String empId,
    String name,
    Shift shiftTimings,
    Integer totalDays,
    List<Map<String, OffsetDateTime>> attended,
    List<LocalDate> leaves
) {}
