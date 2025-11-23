package com.parvis.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Builder
public record EmployeePreferences(
        List<Map<LocalDate, String>> holidays,
        List<String> weekends
) {}
