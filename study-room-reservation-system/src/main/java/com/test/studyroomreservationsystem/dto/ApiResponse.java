package com.test.studyroomreservationsystem.dto;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private String statusCode;
    private String message;
    private T data;

    public ApiResponse(String statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

}
