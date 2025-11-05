package com.parvis.utils;

import java.util.Base64;

public class SessionUtils {

    public static String encode(String empId) {
        return Base64.getEncoder().encodeToString(empId.getBytes());
    }

    public static String decode(String encodedEmpId) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedEmpId);
        return new String(decodedBytes);
    }

}
