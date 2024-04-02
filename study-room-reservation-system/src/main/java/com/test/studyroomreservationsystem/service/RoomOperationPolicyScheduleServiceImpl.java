package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;
import com.test.studyroomreservationsystem.domain.repository.RoomOperationPolicyScheduleRepository;
import com.test.studyroomreservationsystem.dto.roomoperationpolicyschedule.RoomOperationPolicyScheduleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class RoomOperationPolicyScheduleServiceImpl implements RoomOperationPolicyScheduleService{

    private final RoomOperationPolicyScheduleRepository scheduleRepository;

    @Autowired
    public RoomOperationPolicyScheduleServiceImpl(RoomOperationPolicyScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }


    @Override
    public RoomOperationPolicySchedule createSchedule() {
        RoomOperationPolicySchedule schedule = new RoomOperationPolicySchedule();
        return null;
    }

    @Override
    public RoomOperationPolicySchedule updateSchedule(Long roomScheduleId) {
        return null;
    }

    @Override
    public void deleteSchedule(Long roomScheduleId) {
        scheduleRepository.deleteById(roomScheduleId);
    }

    @Override
    public RoomOperationPolicySchedule findScheduleById() {
        return null;
    }

    @Override
    public List<RoomOperationPolicySchedule> findAllSchedule() {
        return null;
    }

    @Override
    public RoomOperationPolicySchedule findByRoomIdAndPolicyDate(Long roomId, LocalDate policyDate) {
        return null;
    }

    @Override
    public RoomOperationPolicyScheduleDto convertToDto(RoomOperationPolicySchedule schedule) {
        return null;
    }
    @Override
    public List<RoomOperationPolicySchedule> findAvailableRoomsGroupedByDateFromToday() {
        LocalDate today = LocalDate.now();
        return scheduleRepository.findAvailableRoomsGroupedByDate(today);
    }


}
