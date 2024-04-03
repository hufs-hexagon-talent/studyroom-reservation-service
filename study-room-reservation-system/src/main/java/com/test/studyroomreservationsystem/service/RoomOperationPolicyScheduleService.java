package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;
import com.test.studyroomreservationsystem.dto.roomoperationpolicyschedule.RoomOperationPolicyScheduleDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomOperationPolicyScheduleService {
    RoomOperationPolicySchedule createSchedule(RoomOperationPolicyScheduleDto scheduleDto);
    RoomOperationPolicySchedule findScheduleById(Long scheduleId);
    List<RoomOperationPolicySchedule> findAllSchedule();
    RoomOperationPolicySchedule updateSchedule(Long scheduleId, RoomOperationPolicyScheduleDto scheduleDto);
    void deleteSchedule(Long roomScheduleId);
    Optional<RoomOperationPolicySchedule> findByRoomIdAndPolicyDate(Long roomId, LocalDate policyDate);
    RoomOperationPolicyScheduleDto convertToDto(RoomOperationPolicySchedule schedule);
    List<RoomOperationPolicySchedule> findAvailableRoomsGroupedByDateFromToday();

}
