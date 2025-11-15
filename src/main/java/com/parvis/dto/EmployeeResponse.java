package com.parvis.dto;

import com.parvis.model.Shift;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record EmployeeResponse (
    String empId,
    String name,
    Shift shiftTimings,
    Integer totalDays,
    List<LocalDate> attended,
    List<LocalDate> leaves
) {}
