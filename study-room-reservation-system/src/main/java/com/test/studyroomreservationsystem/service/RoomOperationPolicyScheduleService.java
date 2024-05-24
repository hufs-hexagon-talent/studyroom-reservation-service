package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;
import com.test.studyroomreservationsystem.dto.roomoperationpolicyschedule.ScheduleRequestDto;
import com.test.studyroomreservationsystem.dto.roomoperationpolicyschedule.ScheduleResponseDto;
import com.test.studyroomreservationsystem.dto.roomoperationpolicyschedule.RoomOperationPolicyScheduleUpdateDto;

import java.time.LocalDate;
import java.util.List;

public interface RoomOperationPolicyScheduleService {
    RoomOperationPolicySchedule createSchedule(ScheduleRequestDto requestDto);
    RoomOperationPolicySchedule findScheduleById(Long scheduleId);
    List<RoomOperationPolicySchedule> findAllSchedule();
    RoomOperationPolicySchedule updateSchedule(Long scheduleId, RoomOperationPolicyScheduleUpdateDto scheduleDto);
    void deleteScheduleById(Long roomScheduleId);
    List<LocalDate> getAvailableDatesFromToday();
    default ScheduleResponseDto dtoFrom(RoomOperationPolicySchedule schedule) {
        return ScheduleResponseDto.builder()
                .roomOperationPolicyScheduleId(schedule.getRoomOperationPolicyScheduleId())
                .roomOperationPolicyId(schedule.getRoomOperationPolicy().getRoomOperationPolicyId())
                .roomId(schedule.getRoom().getRoomId())
                .policyApplicationDate(schedule.getPolicyApplicationDate())
                .build();
    }

}
