package com.test.studyroomreservationsystem.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CheckInRequestDto {
    private String verificationCode;

    private List<Long> roomIds; // 여러 룸들에 대한 ID들
}
