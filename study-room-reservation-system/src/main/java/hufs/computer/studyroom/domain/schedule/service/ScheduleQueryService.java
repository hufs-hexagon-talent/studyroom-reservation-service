package hufs.computer.studyroom.domain.schedule.service;

import hufs.computer.studyroom.common.service.CommonHelperService;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleQueryService {
    private final RoomOperationPolicyScheduleRepository scheduleRepository;
    private final RoomOperationPolicyScheduleMapper scheduleMapper;
    private final CommonHelperService commonHelperService;

    public ScheduleInfoResponse findScheduleById(Long scheduleId) {
        // todo : 추후 validator 리팩토링
        RoomOperationPolicySchedule schedule = commonHelperService.getScheduleById(scheduleId);

        return scheduleMapper.toScheduleInfoResponse(schedule);
    }

    public ScheduleInfoResponses findScheduleByDate(LocalDate date) {
        List<RoomOperationPolicySchedule> schedules = scheduleRepository.findByPolicyApplicationDate(date);
        return scheduleMapper.toScheduleInfoResponses(schedules);
    }

    public AvailableDateResponses getAvailableDatesFromToday() {
        LocalDate today = LocalDate.now();
        List<LocalDate> dates = scheduleRepository.findAvailableRoomsGroupedByDate(today);
        return scheduleMapper.toAvailableDateResponses(dates);
    }
}
