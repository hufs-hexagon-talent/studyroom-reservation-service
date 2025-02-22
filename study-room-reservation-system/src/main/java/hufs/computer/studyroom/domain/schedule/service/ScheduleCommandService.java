package hufs.computer.studyroom.domain.schedule.service;

import hufs.computer.studyroom.common.error.code.ScheduleErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.common.validation.annotation.ExistSchedule;
import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.policy.service.PolicyQueryService;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.room.service.RoomQueryService;
import hufs.computer.studyroom.domain.schedule.dto.request.CreateScheduleBulkRequest;
import hufs.computer.studyroom.domain.schedule.dto.request.ModifyScheduleRequest;
import hufs.computer.studyroom.domain.schedule.dto.response.ScheduleInfoResponse;
import hufs.computer.studyroom.domain.schedule.dto.response.ScheduleInfoResponses;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;
import hufs.computer.studyroom.domain.schedule.mapper.RoomOperationPolicyScheduleMapper;
import hufs.computer.studyroom.domain.schedule.repository.RoomOperationPolicyScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleCommandService {
    private final RoomOperationPolicyScheduleRepository scheduleRepository;
    private final RoomOperationPolicyScheduleMapper scheduleMapper;
    private final PolicyQueryService policyQueryService;
    private final RoomQueryService roomQueryService;
    private final ScheduleQueryService scheduleQueryService;

    @Transactional
    public ScheduleInfoResponses createSchedules(CreateScheduleBulkRequest request) {
        RoomOperationPolicy policy = policyQueryService.getPolicyById(request.roomOperationPolicyId());

        List<LocalDate> dates = request.policyApplicationDates();
        if (dates == null || dates.isEmpty()) {throw new CustomException(ScheduleErrorCode.INVALID_DATES);
        }
        List<Long> roomIds = request.roomIds();
        if (roomIds == null || roomIds.isEmpty()) {throw new CustomException(ScheduleErrorCode.INVALID_ROOM_IDS);
        }

        List<RoomOperationPolicySchedule> createSchedules = new ArrayList<>();

        for (LocalDate date :dates) {
            for (Long roomId : roomIds) {
                // 어떤 날에 대한 스케쥴(운영시간)을 만들때, 그 날에 부여된 스케쥴이 없어야만 함
                Room room = roomQueryService.getRoomById(roomId);

                if (isScheduleAlreadyExists(roomId, date)) { // 예외 처리
                    throw new CustomException(ScheduleErrorCode.SCHEDULE_ALREADY_EXISTS);
                }
                RoomOperationPolicySchedule schedule = scheduleMapper.toRoomOperationPolicySchedule(policy, room, date);
                createSchedules.add(schedule);
            }
        }
        List<RoomOperationPolicySchedule> schedules = scheduleRepository.saveAll(createSchedules);
        return scheduleMapper.toScheduleInfoResponses(schedules);
    }

    public void deleteScheduleById(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    @Transactional
    public ScheduleInfoResponse updateSchedule(Long scheduleId, ModifyScheduleRequest request) {
        RoomOperationPolicySchedule schedule = scheduleQueryService.getScheduleById(scheduleId);
        Room room = roomQueryService.getRoomById(request.roomId());
        RoomOperationPolicy policy = policyQueryService.getPolicyById(request.roomOperationPolicyId());

        scheduleMapper.updateFromRequest(request, room, policy, schedule);

        RoomOperationPolicySchedule updatedSchedule = scheduleRepository.save(schedule);
        return scheduleMapper.toScheduleInfoResponse(updatedSchedule);
    }

    /*
     * util
     * */
    private boolean isScheduleAlreadyExists(Long roomId, LocalDate date) {
        return scheduleRepository.existsByRoomRoomIdAndPolicyApplicationDate(roomId, date);
    }
}
