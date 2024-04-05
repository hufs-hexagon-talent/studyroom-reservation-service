package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.dao.RoomDao;
import com.test.studyroomreservationsystem.dao.RoomOperationPolicyScheduleDao;
import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;
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

    private final RoomOperationPolicyScheduleDao scheduleDao;
    private final RoomDao roomDao;
    private final RoomService roomService;
    private final RoomOperationPolicyService policyService;

    @Autowired
    public RoomOperationPolicyScheduleServiceImpl(RoomOperationPolicyScheduleDao scheduleDao,
                                                  RoomDao roomDao,
                                                  RoomService roomService,
                                                  RoomOperationPolicyService policyService) {
        this.scheduleDao = scheduleDao;
        this.roomDao = roomDao;
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

        return scheduleDao.save(schedule);
    }

    @Override
    public RoomOperationPolicySchedule findScheduleById(Long scheduleId) {
        return scheduleDao.findById(scheduleId)
                .orElseThrow(()->new ScheduleNotFoundException(scheduleId));
    }

    @Override
    public List<RoomOperationPolicySchedule> findAllSchedule() {return scheduleDao.findAll();}
    @Override
    public void deleteScheduleById(Long roomScheduleId) {
        scheduleDao.deleteById(roomScheduleId);
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

        return scheduleDao.save(schedule);
    }
    @Override
    public Optional<RoomOperationPolicySchedule> findByRoomIdAndPolicyDate(Long roomId, LocalDate policyDate) {
        Room room = roomDao.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId));
        return scheduleDao.findByRoomAndPolicyApplicationDate(room, policyDate);
    }


    @Override
    public List<RoomOperationPolicySchedule> findAvailableRoomsGroupedByDateFromToday() {
        LocalDate today = LocalDate.now();
        return scheduleDao.findAvailableRoomsGroupedByDate(today);
    }
    public boolean isExistSchedule(Long roomId , LocalDate date) {
        Room room = roomDao.findById(roomId).orElseThrow(
                () -> new RoomNotFoundException(roomId));

        Optional<RoomOperationPolicySchedule> schedule
                = scheduleDao.findByRoomAndPolicyApplicationDate(room, date);

        // 이게 존재한다면 -> 해당 스케줄 생성 X      false 리턴
        // 이게 존재하지않는다면 -> 해당 스케줄 생성 O    true 리턴

        return schedule.isPresent(); // isEmpty 와 거의 동일함

    }

}
