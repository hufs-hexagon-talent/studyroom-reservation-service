package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;
import com.test.studyroomreservationsystem.domain.repository.RoomOperationPolicyScheduleRepository;
import com.test.studyroomreservationsystem.domain.repository.RoomRepository;
import com.test.studyroomreservationsystem.dto.roomoperationpolicyschedule.RoomOperationPolicyScheduleDto;
import com.test.studyroomreservationsystem.dto.roomoperationpolicyschedule.RoomOperationPolicyScheduleUpdateDto;
import com.test.studyroomreservationsystem.service.RoomOperationPolicyScheduleService;
import com.test.studyroomreservationsystem.service.RoomOperationPolicyService;
import com.test.studyroomreservationsystem.service.RoomService;
import com.test.studyroomreservationsystem.service.exception.RoomNotFoundException;
import com.test.studyroomreservationsystem.service.exception.ScheduleAlreadyExistException;
import com.test.studyroomreservationsystem.service.exception.ScheduleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RoomOperationPolicyScheduleServiceImpl implements RoomOperationPolicyScheduleService {

    private final RoomOperationPolicyScheduleRepository scheduleRepository;
    private final RoomRepository roomRepository;
    private final RoomService roomService;
    private final RoomOperationPolicyService policyService;

    @Autowired
    public RoomOperationPolicyScheduleServiceImpl(RoomOperationPolicyScheduleRepository scheduleRepository,
                                                  RoomRepository roomRepository,
                                                  RoomService roomService,
                                                  RoomOperationPolicyService policyService) {
        this.scheduleRepository = scheduleRepository;
        this.roomRepository = roomRepository;
        this.roomService = roomService;
        this.policyService = policyService;
    }



    @Override
    public RoomOperationPolicySchedule createSchedule(RoomOperationPolicyScheduleDto scheduleDto) {
        Long roomOperationPolicyId = scheduleDto.getRoomOperationPolicyId();
        Long roomId = scheduleDto.getRoomId();
        LocalDate date = scheduleDto.getPolicyApplicationDate();
        // 어떤 날에 대한 스케쥴(운영시간)을 만들때, 그 날에 부여된 스케쥴이 없어야만 함
        if (isExistSchedule(roomId, date)) {
        // 예외 처리
            throw new ScheduleAlreadyExistException(roomId,date);
        }

        RoomOperationPolicySchedule schedule = new RoomOperationPolicySchedule();
        schedule.setRoom(roomService.findRoomById(roomId));
        schedule.setRoomOperationPolicy(policyService.findPolicyById(roomOperationPolicyId));
        schedule.setPolicyApplicationDate(date);

        return scheduleRepository.save(schedule);
    }

    @Override
    public RoomOperationPolicySchedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(()->new ScheduleNotFoundException(scheduleId));
    }

    @Override
    public List<RoomOperationPolicySchedule> findAllSchedule() {return scheduleRepository.findAll();}
    @Override
    public void deleteScheduleById(Long roomScheduleId) {
        scheduleRepository.deleteById(roomScheduleId);
    }

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
    @Override
    public Optional<RoomOperationPolicySchedule> findByRoomIdAndPolicyDate(Long roomId, LocalDate policyDate) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId));
        return scheduleRepository.findByRoomAndPolicyApplicationDate(room, policyDate);
    }

    @Override
    public RoomOperationPolicyScheduleDto convertToDto(RoomOperationPolicySchedule schedule) {
        RoomOperationPolicyScheduleDto roomOperationPolicyScheduleDto = new RoomOperationPolicyScheduleDto();

        roomOperationPolicyScheduleDto.setRoomOperationPolicyId(schedule.getRoomOperationPolicy().getRoomOperationPolicyId());
        roomOperationPolicyScheduleDto.setRoomId(schedule.getRoom().getRoomId());
        roomOperationPolicyScheduleDto.setPolicyApplicationDate(schedule.getPolicyApplicationDate());
        return roomOperationPolicyScheduleDto;
    }
    @Override
    public List<RoomOperationPolicySchedule> findAvailableRoomsGroupedByDateFromToday() {
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
