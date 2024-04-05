package com.test.studyroomreservationsystem.dao.impl;

import com.test.studyroomreservationsystem.dao.RoomOperationPolicyScheduleDao;
import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;
import com.test.studyroomreservationsystem.domain.repository.RoomOperationPolicyScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class RoomOperationPolicyScheduleDaoImpl implements RoomOperationPolicyScheduleDao {
    private final RoomOperationPolicyScheduleRepository scheduleRepository;
    @Autowired
    public RoomOperationPolicyScheduleDaoImpl(RoomOperationPolicyScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public RoomOperationPolicySchedule save(RoomOperationPolicySchedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Override
    public Optional<RoomOperationPolicySchedule> findById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId);
    }

    @Override
    public List<RoomOperationPolicySchedule> findAll() {
        return scheduleRepository.findAll();
    }

    @Override
    public void deleteById(Long roomScheduleId) {
        scheduleRepository.deleteById(roomScheduleId);
    }

    @Override
    public Optional<RoomOperationPolicySchedule> findByRoomAndPolicyApplicationDate(Room room, LocalDate policyDate) {
        return scheduleRepository.findByRoomAndPolicyApplicationDate(room, policyDate);
    }

    @Override
    public List<RoomOperationPolicySchedule> findAvailableRoomsGroupedByDate(LocalDate today) {
        return scheduleRepository.findAvailableRoomsGroupedByDate(today);
    }
}
