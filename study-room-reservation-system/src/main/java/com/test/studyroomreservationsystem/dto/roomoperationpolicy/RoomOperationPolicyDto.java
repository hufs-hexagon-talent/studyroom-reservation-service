package com.test.studyroomreservationsystem.dto.roomoperationpolicy;

import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class RoomOperationPolicyDto {
    private LocalTime operationStartTime;
    private LocalTime operationEndTime;
    private Integer eachMaxMinute;

    @Builder
    public RoomOperationPolicyDto(LocalTime operationStartTime, LocalTime operationEndTime, Integer eachMaxMinute) {
        this.operationStartTime = operationStartTime;
        this.operationEndTime = operationEndTime;
        this.eachMaxMinute = eachMaxMinute;
    }
    public RoomOperationPolicy toEntity() {
        return RoomOperationPolicy.builder()
                .operationStartTime(operationStartTime)
                .operationEndTime(operationEndTime)
                .eachMaxMinute(eachMaxMinute)
                .build();
    }
}
