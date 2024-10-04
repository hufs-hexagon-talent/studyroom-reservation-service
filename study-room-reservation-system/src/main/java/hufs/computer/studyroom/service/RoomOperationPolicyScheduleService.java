package hufs.computer.studyroom.service;

import hufs.computer.studyroom.domain.entity.Room;
import hufs.computer.studyroom.domain.entity.RoomOperationPolicySchedule;
import hufs.computer.studyroom.dto.operationpolicyschedule.ScheduleRequestDto;
import hufs.computer.studyroom.dto.operationpolicyschedule.ScheduleResponseDto;
import hufs.computer.studyroom.dto.operationpolicyschedule.RoomOperationPolicyScheduleUpdateDto;

import java.time.LocalDate;
import java.util.List;

public interface RoomOperationPolicyScheduleService {
    List<RoomOperationPolicySchedule> createSchedules(ScheduleRequestDto requestDto);
    RoomOperationPolicySchedule findScheduleById(Long scheduleId);
    List<RoomOperationPolicySchedule> findScheduleByDate(LocalDate date);
    List<RoomOperationPolicySchedule> findAllSchedule();
    RoomOperationPolicySchedule updateSchedule(Long scheduleId, RoomOperationPolicyScheduleUpdateDto scheduleDto);
    void deleteScheduleById(Long roomScheduleId);
    RoomOperationPolicySchedule findScheduleByRoomAndDate(Room room, LocalDate date);
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
