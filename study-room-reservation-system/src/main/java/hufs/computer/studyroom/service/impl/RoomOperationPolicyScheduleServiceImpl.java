package hufs.computer.studyroom.service.impl;

import hufs.computer.studyroom.domain.entity.Room;
import hufs.computer.studyroom.domain.entity.RoomOperationPolicy;
import hufs.computer.studyroom.domain.entity.RoomOperationPolicySchedule;
import hufs.computer.studyroom.domain.repository.RoomOperationPolicyScheduleRepository;
import hufs.computer.studyroom.domain.repository.RoomRepository;
import hufs.computer.studyroom.dto.operationpolicyschedule.ScheduleRequestDto;
import hufs.computer.studyroom.dto.operationpolicyschedule.RoomOperationPolicyScheduleUpdateDto;
import hufs.computer.studyroom.exception.invalidvalue.InvalidDatesException;
import hufs.computer.studyroom.exception.invalidvalue.InvalidRoomIdsException;
import hufs.computer.studyroom.service.RoomOperationPolicyScheduleService;
import hufs.computer.studyroom.service.RoomOperationPolicyService;
import hufs.computer.studyroom.service.RoomService;
import hufs.computer.studyroom.exception.notfound.RoomNotFoundException;
import hufs.computer.studyroom.exception.administrative.ScheduleAlreadyExistException;
import hufs.computer.studyroom.exception.notfound.ScheduleNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomOperationPolicyScheduleServiceImpl implements RoomOperationPolicyScheduleService {
    private final RoomOperationPolicyScheduleRepository scheduleRepository;
    private final RoomRepository roomRepository;
    private final RoomService roomService;
    private final RoomOperationPolicyService policyService;

    public RoomOperationPolicyScheduleServiceImpl(RoomOperationPolicyScheduleRepository scheduleRepository, RoomRepository roomRepository, RoomService roomService, RoomOperationPolicyService policyService) {
        this.scheduleRepository = scheduleRepository;
        this.roomRepository = roomRepository;
        this.roomService = roomService;
        this.policyService = policyService;
    }


    @Override
    public List<RoomOperationPolicySchedule> createSchedules(ScheduleRequestDto requestDto) {
        Long roomOperationPolicyId = requestDto.getRoomOperationPolicyId();
        RoomOperationPolicy policy = policyService.findPolicyById(roomOperationPolicyId);
        List<LocalDate> dates = requestDto.getPolicyApplicationDates();
        if (dates == null || dates.isEmpty()) {
            throw new InvalidDatesException();
        }
        List<Long> roomIds = requestDto.getRoomIds();
        if (roomIds == null || roomIds.isEmpty()) {
            throw new InvalidRoomIdsException();
        }
        List<RoomOperationPolicySchedule> createSchedules = new ArrayList<>();

        for (LocalDate date :dates) {
            for (Long roomId : roomIds) {
                // 어떤 날에 대한 스케쥴(운영시간)을 만들때, 그 날에 부여된 스케쥴이 없어야만 함
                Room room = roomService.findRoomById(roomId);
                if (isExistSchedule(roomId, date)) {
                    // 예외 처리
                    throw new ScheduleAlreadyExistException(roomId,date);
                }
                RoomOperationPolicySchedule schedule = requestDto.toEntity(policy, room, date);
                createSchedules.add(schedule);
            }

        }
        return scheduleRepository.saveAll(createSchedules);
    }

    @Override
    public RoomOperationPolicySchedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(()->new ScheduleNotFoundException(scheduleId));
    }

    @Override
    public List<RoomOperationPolicySchedule> findScheduleByDate(LocalDate date) {
        return scheduleRepository.findByPolicyApplicationDate(date);
    }

    @Override
    public List<RoomOperationPolicySchedule> findAllSchedule() {return scheduleRepository.findAll();}
    @Override
    public void deleteScheduleById(Long roomScheduleId) {
        findScheduleById(roomScheduleId); // 찾아보고 없으면 예외처리
        scheduleRepository.deleteById(roomScheduleId);
    }

    @Override
    public RoomOperationPolicySchedule findScheduleByRoomAndDate(Room room, LocalDate date) {
        return scheduleRepository.findByRoomAndPolicyApplicationDate(room, date).orElseThrow(
                () -> new ScheduleNotFoundException(room,date)
        );
    }


    // todo : 변경
    @Override
    public RoomOperationPolicySchedule updateSchedule(Long scheduleId, RoomOperationPolicyScheduleUpdateDto scheduleDto) {
        RoomOperationPolicySchedule schedule = findScheduleById(scheduleId);

        Long roomOperationPolicyId = scheduleDto.getRoomOperationPolicyId();
        Long roomId = scheduleDto.getRoomId();
        LocalDate date = scheduleDto.getPolicyApplicationDate();

        // 어떤 날에 대한 스케쥴(운영시간)로 변경할 때, 그 날에 부여된 스케쥴이 없어야만 함
        if (isExistSchedule(roomId, date)) {
            // 예외 처리
            throw new ScheduleAlreadyExistException(roomId,date);
        }
        schedule.setRoomOperationPolicy(policyService.findPolicyById(roomOperationPolicyId));
        schedule.setRoom(roomService.findRoomById(roomId));
        schedule.setPolicyApplicationDate(date);

        return scheduleRepository.save(schedule);
    }

    // todo 변경
    @Override
    public List<LocalDate> getAvailableDatesFromToday() {
        LocalDate today = LocalDate.now();
        return scheduleRepository.findAvailableRoomsGroupedByDate(today);
    }


    public boolean isExistSchedule(Long roomId , LocalDate date) {
        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new RoomNotFoundException(roomId));

        Optional<RoomOperationPolicySchedule> schedule
                = scheduleRepository.findByRoomAndPolicyApplicationDate(room, date);

        // 이게 존재한다면 -> 해당 스케줄 생성 X      false 리턴
        // 이게 존재하지않는다면 -> 해당 스케줄 생성 O    true 리턴

        return schedule.isPresent(); // isEmpty 와 거의 동일함

    }

}
