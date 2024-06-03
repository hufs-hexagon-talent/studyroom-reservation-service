package com.test.studyroomreservationsystem.dto.util;

import lombok.Getter;

@Getter
//@Builder
public class ErrorResponseDto {
    private final String status;
    private final String errorMessage;


    public ErrorResponseDto(String status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }
}
