package hufs.computer.studyroom.domain.schedule.service;

import hufs.computer.studyroom.common.error.code.PolicyErrorCode;
import hufs.computer.studyroom.common.error.code.RoomErrorCode;
import hufs.computer.studyroom.common.error.code.ScheduleErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.common.error.exception.todo.notfound.ScheduleNotFoundException;
import hufs.computer.studyroom.domain.policy.repository.RoomOperationPolicyRepository;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.schedule.dto.request.CreateScheduleBulkRequest;
import hufs.computer.studyroom.domain.schedule.dto.response.AvailableDateResponses;
import hufs.computer.studyroom.domain.schedule.dto.response.ScheduleInfoResponse;
import hufs.computer.studyroom.domain.schedule.dto.response.ScheduleInfoResponses;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;
import hufs.computer.studyroom.domain.schedule.mapper.RoomOperationPolicyScheduleMapper;
import hufs.computer.studyroom.domain.schedule.repository.RoomOperationPolicyScheduleRepository;
import hufs.computer.studyroom.domain.room.repository.RoomRepository;
import hufs.computer.studyroom.domain.schedule.dto.request.ModifyScheduleRequest;
import hufs.computer.studyroom.domain.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomOperationPolicyScheduleServiceImpl implements RoomOperationPolicyScheduleService {
    private final RoomOperationPolicyScheduleRepository scheduleRepository;
    private final RoomRepository roomRepository;
    private final RoomService roomService;
    private final RoomOperationPolicyRepository policyRepository;
    private final RoomOperationPolicyScheduleMapper scheduleMapper;

    /*
    * Command
    * */
    @Override
    public ScheduleInfoResponses createSchedules(CreateScheduleBulkRequest request) {
        RoomOperationPolicy policy = policyRepository.findById(request.roomOperationPolicyId()).orElseThrow(() -> new CustomException(PolicyErrorCode.POLICY_NOT_FOUND));

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
                Room room = roomRepository.findById(roomId).orElseThrow(() -> new CustomException(RoomErrorCode.ROOM_NOT_FOUND));
                if (isScheduleAlreadyExists(roomId, date, null)) { // 예외 처리
                    throw new CustomException(ScheduleErrorCode.SCHEDULE_ALREADY_EXISTS);
                }
                RoomOperationPolicySchedule schedule = scheduleMapper.toRoomOperationPolicySchedule(policy, room, date);
                createSchedules.add(schedule);
            }
        }
        List<RoomOperationPolicySchedule> schedules = scheduleRepository.saveAll(createSchedules);
        return scheduleMapper.toScheduleInfoResponses(schedules);
    }
    /*
     * Command
     * */
    @Override
    public void deleteScheduleById(Long roomScheduleId) {
        findScheduleById(roomScheduleId); // 찾아보고 없으면 예외처리
        scheduleRepository.deleteById(roomScheduleId);
    }

    /*
    * Query
    * */
    @Override
    public ScheduleInfoResponse findScheduleById(Long scheduleId) {
        RoomOperationPolicySchedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new CustomException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
        return scheduleMapper.toScheduleInfoResponse(schedule);
    }
    /*
     * Query
     * */
    @Override
    public ScheduleInfoResponses findScheduleByDate(LocalDate date) {
        List<RoomOperationPolicySchedule> schedules = scheduleRepository.findByPolicyApplicationDate(date);
        return scheduleMapper.toScheduleInfoResponses(schedules);
    }



    @Override
    public RoomOperationPolicySchedule findScheduleByRoomAndDate(Room room, LocalDate date) {
        return scheduleRepository.findByRoomAndPolicyApplicationDate(room, date).orElseThrow(() -> new CustomException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));
    }


    @Override
    @Transactional
    public ScheduleInfoResponse updateSchedule(Long scheduleId, ModifyScheduleRequest request) {
        RoomOperationPolicySchedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new CustomException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        // 운영 정책 업데이트
        if (request.roomOperationPolicyId() != null) {
            RoomOperationPolicy policy = policyRepository.findById(request.roomOperationPolicyId())
                    .orElseThrow(() -> new CustomException(PolicyErrorCode.POLICY_NOT_FOUND));
            schedule.setRoomOperationPolicy(policy);
        }

        // 룸 업데이트
        if (request.roomId() != null) {
            Room room = roomRepository.findById(request.roomId())
                    .orElseThrow(() -> new CustomException(RoomErrorCode.ROOM_NOT_FOUND));
            schedule.setRoom(room);
        }

        // 날짜 업데이트
        if (request.policyApplicationDate() != null) {
            LocalDate date = request.policyApplicationDate();

            // 다른 스케줄과 날짜 겹침 확인 (자기 자신을 제외하고)
            if (isScheduleAlreadyExists(request.roomId(), date, scheduleId)) {
                throw new CustomException(ScheduleErrorCode.SCHEDULE_ALREADY_EXISTS);
            }
            schedule.setPolicyApplicationDate(date);
        }

        RoomOperationPolicySchedule updatedSchedule = scheduleRepository.save(schedule);
        return scheduleMapper.toScheduleInfoResponse(updatedSchedule);
    }


    @Override
    public AvailableDateResponses getAvailableDatesFromToday() {
        LocalDate today = LocalDate.now();
        List<LocalDate> dates = scheduleRepository.findAvailableRoomsGroupedByDate(today);
        return scheduleMapper.toAvailableDateResponses(dates);
    }

    /*
    * util
    * */
    private boolean isScheduleAlreadyExists(Long roomId, LocalDate date, Long excludeScheduleId) {
        return scheduleRepository.existsByRoomIdAndPolicyApplicationDateAndRoomOperationPolicyScheduleIdNot(
                roomId, date, excludeScheduleId
        );
    }

}
