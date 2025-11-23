package com.parvis.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.Optional;

@Builder
public record EmployeeAttendanceRequest(
        OffsetDateTime inTime,
        OffsetDateTime outTime,
        MultipartFile image
) {}
