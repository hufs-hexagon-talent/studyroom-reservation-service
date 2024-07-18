package com.test.studyroomreservationsystem.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CheckInRequestDto {
    private String verificationCode;

    private List<Long> roomIds; // 여러 룸들에 대한 ID들

    public CheckInRequestDto(String verificationCode, List<Long> roomIds) {
        this.verificationCode = verificationCode;
        this.roomIds = roomIds;
    }
}
