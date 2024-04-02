package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;
import com.test.studyroomreservationsystem.dto.roomoperationpolicyschedule.RoomOperationPolicyScheduleDto;

import java.time.LocalDate;
import java.util.List;

public interface RoomOperationPolicyScheduleService {
    RoomOperationPolicySchedule createSchedule();
    RoomOperationPolicySchedule updateSchedule(Long roomScheduleId);
    void deleteSchedule(Long roomScheduleId);
    RoomOperationPolicySchedule findScheduleById();
    List<RoomOperationPolicySchedule> findAllSchedule();
    RoomOperationPolicySchedule findByRoomIdAndPolicyDate(Long roomId, LocalDate policyDate);
    RoomOperationPolicyScheduleDto convertToDto(RoomOperationPolicySchedule schedule);
    List<RoomOperationPolicySchedule> findAvailableRoomsGroupedByDateFromToday();

}
