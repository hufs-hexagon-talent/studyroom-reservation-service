package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;
import com.test.studyroomreservationsystem.dto.roomoperationpolicyschedule.RoomOperationPolicyScheduleDto;
import com.test.studyroomreservationsystem.dto.roomoperationpolicyschedule.RoomOperationPolicyScheduleUpdateDto;

import java.time.LocalDate;
import java.util.List;

public interface RoomOperationPolicyScheduleService {
    RoomOperationPolicySchedule createSchedule(RoomOperationPolicyScheduleDto scheduleDto);
    RoomOperationPolicySchedule findScheduleById(Long scheduleId);
    List<RoomOperationPolicySchedule> findAllSchedule();
    RoomOperationPolicySchedule updateSchedule(Long scheduleId, RoomOperationPolicyScheduleUpdateDto scheduleDto);
    void deleteScheduleById(Long roomScheduleId);
    List<LocalDate> getAvailableDatesFromToday();
    default RoomOperationPolicyScheduleDto dtoFrom(RoomOperationPolicySchedule schedule) {
        return RoomOperationPolicyScheduleDto.builder()
                .roomOperationPolicyId(schedule.getRoomOperationPolicyScheduleId())
                .roomId(schedule.getRoom().getRoomId())
                .policyApplicationDate(schedule.getPolicyApplicationDate())
                .build();
    }

}
