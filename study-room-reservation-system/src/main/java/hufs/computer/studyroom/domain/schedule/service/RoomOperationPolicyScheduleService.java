package hufs.computer.studyroom.domain.schedule.service;

import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.schedule.dto.request.CreateScheduleBulkRequest;
import hufs.computer.studyroom.domain.schedule.dto.response.AvailableDateResponses;
import hufs.computer.studyroom.domain.schedule.dto.response.ScheduleInfoResponse;
import hufs.computer.studyroom.domain.schedule.dto.response.ScheduleInfoResponses;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;
import hufs.computer.studyroom.domain.schedule.dto.request.ModifyScheduleRequest;

import java.time.LocalDate;

public interface RoomOperationPolicyScheduleService {
    ScheduleInfoResponses createSchedules(CreateScheduleBulkRequest request);
    ScheduleInfoResponse findScheduleById(Long scheduleId);
    ScheduleInfoResponses findScheduleByDate(LocalDate date);
    ScheduleInfoResponse updateSchedule(Long scheduleId, ModifyScheduleRequest scheduleDto);
    void deleteScheduleById(Long roomScheduleId);
    RoomOperationPolicySchedule findScheduleByRoomAndDate(Room room, LocalDate date);
    AvailableDateResponses getAvailableDatesFromToday();

}
