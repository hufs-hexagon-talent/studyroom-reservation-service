package com.test.studyroomreservationsystem.dto;

import lombok.Getter;

@Getter
public class ApiResponseDto<T> {
    private String statusCode;
    private String message;
    private T data;

    public ApiResponseDto(String statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

}
