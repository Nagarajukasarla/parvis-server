package com.parvis.service;

import com.parvis.dto.EmployeeAttendanceRequest;
import com.parvis.dto.EmployeePreferences;
import com.parvis.dto.EmployeeResponse;
import com.parvis.factory.AppResponse;

public interface EmployeeService {

    AppResponse<EmployeeResponse> getEmployeeProfile(String empId);

    AppResponse<String> markAttendance(String empId, EmployeeAttendanceRequest request);

    AppResponse<EmployeePreferences> getEmployeePreferences();

    AppResponse<?> hasMarkedInToday(String empId);
}
