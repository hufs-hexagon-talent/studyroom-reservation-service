package com.test.studyroomreservationsystem.apicontroller.admin;

import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;
import com.test.studyroomreservationsystem.dto.roomoperationpolicyschedule.RoomOperationPolicyScheduleDto;
import com.test.studyroomreservationsystem.dto.roomoperationpolicyschedule.RoomOperationPolicyScheduleUpdateDto;
import com.test.studyroomreservationsystem.service.impl.RoomOperationPolicyScheduleServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "PolicySchedule", description = "날짜에 따른 방운영 정책")
@RestController
@RequestMapping("/schedules")
public class AdminScheduleController {
    private final RoomOperationPolicyScheduleServiceImpl scheduleService;

    @Autowired
    public AdminScheduleController(RoomOperationPolicyScheduleServiceImpl scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Operation(summary = "✅[관리자] schedule 생성",
            description = "스케쥴 생성(상세설명 추후 추가)",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PostMapping("/schedule")
     ResponseEntity<RoomOperationPolicyScheduleDto> getScheduleById(@RequestBody RoomOperationPolicyScheduleDto scheduleDto) {

        RoomOperationPolicySchedule createdSchedule
                = scheduleService.createSchedule(scheduleDto);

        RoomOperationPolicyScheduleDto createdScheduleDto
                = scheduleService.dtoFrom(createdSchedule);
        return new ResponseEntity<>(createdScheduleDto, HttpStatus.CREATED);
    }
    // todo : 날짜를 주어지면 해당 날짜의 모든 방의 정책을 조회



    @Operation(summary = "❌[관리자] schedule 조회",
            description = "스케쥴 조회",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping("/schedule")
     ResponseEntity<RoomOperationPolicyScheduleDto> getScheduleById(@PathVariable Long roomOperationPolicyScheduleId) {

        RoomOperationPolicySchedule foundSchedule
                = scheduleService.findScheduleById(roomOperationPolicyScheduleId);

        RoomOperationPolicyScheduleDto foundScheduleDto
                = scheduleService.dtoFrom(foundSchedule);

        return new ResponseEntity<>(foundScheduleDto,HttpStatus.OK);
    }


    @Operation(summary = "❌[관리자] schedule 업데이트",
            description = "해당 스케쥴 업데이트",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PutMapping("/schedule")
    ResponseEntity<RoomOperationPolicyScheduleDto>
    updateSchedule(@PathVariable Long roomOperationPolicyScheduleId,
                   @RequestBody RoomOperationPolicyScheduleUpdateDto scheduleUpdateDto) {

        RoomOperationPolicySchedule updatedSchedule
                = scheduleService.updateSchedule(roomOperationPolicyScheduleId, scheduleUpdateDto);

        RoomOperationPolicyScheduleDto updatedScheduleDto
                = scheduleService.dtoFrom(updatedSchedule);

        return new ResponseEntity<>(updatedScheduleDto, HttpStatus.OK);
    }

    @Operation(summary = "❌[관리자] schedule 삭제",
            description = "해당 schedule id의 정보 삭제 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @DeleteMapping("/schedule")
     ResponseEntity<Void> deleteSchedule(@PathVariable Long roomOperationPolicyScheduleId) {
        scheduleService.deleteScheduleById(roomOperationPolicyScheduleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}


