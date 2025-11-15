package com.parvis.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;

@Builder
public record EmployeeAttendanceRequest(
    String userId,
    String name,
    OffsetDateTime dateTime,
    MultipartFile image
) {}
