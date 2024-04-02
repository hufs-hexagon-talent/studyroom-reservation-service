package com.test.studyroomreservationsystem.apicontroller.user;

import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;
import com.test.studyroomreservationsystem.service.RoomOperationPolicyScheduleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/user/schedules")
public class RoomOperationPolicyScheduleController {
    private final RoomOperationPolicyScheduleServiceImpl scheduleService;
    @Autowired
    public RoomOperationPolicyScheduleController(RoomOperationPolicyScheduleServiceImpl scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/available")
    public ResponseEntity<List<RoomOperationPolicySchedule>> getAvailableRoomsGroupedByDate() {
        List<RoomOperationPolicySchedule> availableRooms
                = scheduleService.findAvailableRoomsGroupedByDateFromToday();
        return ResponseEntity.ok(availableRooms);
    }
}


