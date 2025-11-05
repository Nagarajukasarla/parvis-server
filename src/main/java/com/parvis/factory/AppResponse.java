package com.parvis.factory;

public record AppResponse<T>(boolean success, T data, ErrorDetails errorDetails) {

    public static <T> AppResponse<T> success(T data) {
        return new AppResponse<T>(true, data, null);
    }

    public static <T> AppResponse<T> failure(ErrorDetails errorDetails) {
        return new AppResponse<T>(false, null, errorDetails);
    }

}
