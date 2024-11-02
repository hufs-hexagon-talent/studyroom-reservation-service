package hufs.computer.studyroom.domain.schedule.service;

import hufs.computer.studyroom.common.error.code.ScheduleErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.schedule.dto.response.AvailableDateResponses;
import hufs.computer.studyroom.domain.schedule.dto.response.ScheduleInfoResponse;
import hufs.computer.studyroom.domain.schedule.dto.response.ScheduleInfoResponses;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;
import hufs.computer.studyroom.domain.schedule.mapper.RoomOperationPolicyScheduleMapper;
import hufs.computer.studyroom.domain.schedule.repository.RoomOperationPolicyScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleQueryService {
    private final RoomOperationPolicyScheduleRepository scheduleRepository;
    private final RoomOperationPolicyScheduleMapper scheduleMapper;

    public ScheduleInfoResponse findScheduleById(Long scheduleId) {
        RoomOperationPolicySchedule schedule = getScheduleById(scheduleId);

        return scheduleMapper.toScheduleInfoResponse(schedule);
    }

    public ScheduleInfoResponses findScheduleByDate(LocalDate date) {
        List<RoomOperationPolicySchedule> schedules = scheduleRepository.findByPolicyApplicationDate(date);
        return scheduleMapper.toScheduleInfoResponses(schedules);
    }

    public AvailableDateResponses getAvailableDatesFromToday(Long departmentId) {
        LocalDate today = LocalDate.now();
        List<LocalDate> dates = scheduleRepository.findAvailableRoomsGroupedByDate(today, departmentId);
        return scheduleMapper.toAvailableDateResponses(dates);
    }

    public RoomOperationPolicySchedule getScheduleById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new CustomException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
    }

    public Optional<RoomOperationPolicySchedule> getScheduleByRoomAndDate(Room room, LocalDate date) {
        return scheduleRepository.findByRoomAndPolicyApplicationDate(room, date);
        //.orElseThrow(() -> new CustomException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
    }
    public boolean existByScheduleId(Long scheduleId) {
        return scheduleRepository.existsById(scheduleId);
    }
}
