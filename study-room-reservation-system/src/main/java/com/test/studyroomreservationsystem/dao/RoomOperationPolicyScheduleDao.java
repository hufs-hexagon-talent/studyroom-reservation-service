package com.test.studyroomreservationsystem.dao;

import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomOperationPolicyScheduleDao {
    RoomOperationPolicySchedule save(RoomOperationPolicySchedule schedule);
    Optional<RoomOperationPolicySchedule> findById(Long scheduleId);
    List<RoomOperationPolicySchedule> findAll();
    void deleteById(Long roomScheduleId);
    Optional<RoomOperationPolicySchedule> findByRoomAndPolicyApplicationDate(Room room, LocalDate policyDate);
    List<RoomOperationPolicySchedule> findAvailableRoomsGroupedByDate(LocalDate today);
}
