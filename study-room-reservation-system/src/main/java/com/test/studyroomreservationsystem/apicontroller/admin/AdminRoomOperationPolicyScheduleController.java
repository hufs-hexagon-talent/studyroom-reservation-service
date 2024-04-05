package com.test.studyroomreservationsystem.apicontroller.admin;

import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;
import com.test.studyroomreservationsystem.dto.roomoperationpolicyschedule.RoomOperationPolicyScheduleDto;
import com.test.studyroomreservationsystem.dto.roomoperationpolicyschedule.RoomOperationPolicyScheduleUpdateDto;
import com.test.studyroomreservationsystem.service.RoomOperationPolicyScheduleServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "PolicySchedule", description = "날짜에 따른 방운영 정책")
@RestController
@RequestMapping("/admin/schedules")
public class AdminRoomOperationPolicyScheduleController {
    private final RoomOperationPolicyScheduleServiceImpl scheduleService;

    @Autowired
    AdminRoomOperationPolicyScheduleController(RoomOperationPolicyScheduleServiceImpl scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Operation(summary = "schedule 생성", description = "스케쥴 생성(상세설명 추후 추가)")
    @PostMapping
     ResponseEntity<RoomOperationPolicyScheduleDto> getScheduleById(@RequestBody RoomOperationPolicyScheduleDto scheduleDto) {

        RoomOperationPolicySchedule createdSchedule
                = scheduleService.createSchedule(scheduleDto);

        RoomOperationPolicyScheduleDto createdScheduleDto
                = scheduleService.convertToDto(createdSchedule);
        return new ResponseEntity<>(createdScheduleDto, HttpStatus.CREATED);
    }

    @Operation(summary = "schedule 조회", description = "스케쥴 조회")
    @GetMapping("/{roomOperationPolicyScheduleId}")
     ResponseEntity<RoomOperationPolicyScheduleDto> getScheduleById(@PathVariable Long roomOperationPolicyScheduleId) {

        RoomOperationPolicySchedule foundSchedule
                = scheduleService.findScheduleById(roomOperationPolicyScheduleId);

        RoomOperationPolicyScheduleDto foundScheduleDto
                = scheduleService.convertToDto(foundSchedule);

        return new ResponseEntity<>(foundScheduleDto,HttpStatus.OK);
    }

    @Operation(summary = "현재로 부터 미래까지 운영 예정인 방들 조회", description = "현재로 부터 예약가능한 방들을 날짜를 기준으로 묶어 조회")
    @GetMapping("/available")
     ResponseEntity<List<RoomOperationPolicyScheduleDto>> getAvailableRoomsGroupedByDate() {

        List<RoomOperationPolicyScheduleDto> availableRooms
                = scheduleService.findAvailableRoomsGroupedByDateFromToday()
                .stream()
                .map(scheduleService::convertToDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(availableRooms, HttpStatus.OK);
    }


    @Operation(summary = "schedule 업데이트", description = "해당 스케쥴 업데이트")
    @PutMapping("/{roomOperationPolicyScheduleId}")
    ResponseEntity<RoomOperationPolicyScheduleDto>
    updateSchedule(@PathVariable Long roomOperationPolicyScheduleId,
                   @RequestBody RoomOperationPolicyScheduleUpdateDto scheduleUpdateDto) {

        RoomOperationPolicySchedule updatedSchedule
                = scheduleService.updateSchedule(roomOperationPolicyScheduleId, scheduleUpdateDto);

        RoomOperationPolicyScheduleDto updatedScheduleDto
                = scheduleService.convertToDto(updatedSchedule);

        return new ResponseEntity<>(updatedScheduleDto, HttpStatus.OK);
    }

    @Operation(summary = "schedule 삭제", description = "해당 schedule id의 정보 삭제 API")
    @DeleteMapping("/{roomOperationPolicyScheduleId}")
     ResponseEntity<Void> deleteSchedule(@PathVariable Long roomOperationPolicyScheduleId) {
        scheduleService.deleteScheduleById(roomOperationPolicyScheduleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}


