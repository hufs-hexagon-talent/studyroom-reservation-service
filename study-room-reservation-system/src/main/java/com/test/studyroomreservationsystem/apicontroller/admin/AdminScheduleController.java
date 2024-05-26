package com.test.studyroomreservationsystem.apicontroller.admin;

import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;
import com.test.studyroomreservationsystem.dto.ApiResponseDto;
import com.test.studyroomreservationsystem.dto.roomoperationpolicyschedule.ScheduleRequestDto;
import com.test.studyroomreservationsystem.dto.roomoperationpolicyschedule.ScheduleResponseDto;
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
    @PostMapping()
     ResponseEntity<ApiResponseDto<ScheduleResponseDto>> getScheduleById(@RequestBody ScheduleRequestDto requestDto) {

        RoomOperationPolicySchedule createdSchedule
                = scheduleService.createSchedule(requestDto);

        ScheduleResponseDto createdScheduleDto
                = scheduleService.dtoFrom(createdSchedule);

        ApiResponseDto<ScheduleResponseDto> response
                = new ApiResponseDto<>(HttpStatus.CREATED.toString(), "정상적으로 생성 되었습니다.", createdScheduleDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @Operation(summary = "✅[관리자] schedule 조회",
            description = "스케쥴 조회",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping("/{roomOperationPolicyScheduleId}")
     ResponseEntity<ApiResponseDto<ScheduleResponseDto>> getScheduleById(@PathVariable Long roomOperationPolicyScheduleId) {

        RoomOperationPolicySchedule foundSchedule
                = scheduleService.findScheduleById(roomOperationPolicyScheduleId);

        ScheduleResponseDto foundScheduleDto
                = scheduleService.dtoFrom(foundSchedule);
        ApiResponseDto<ScheduleResponseDto> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", foundScheduleDto);

        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @Operation(summary = "❌[관리자] schedule 업데이트",
            description = "해당 스케쥴 업데이트",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PutMapping("/schedule")
    ResponseEntity<ScheduleResponseDto>
    updateSchedule(@PathVariable Long roomOperationPolicyScheduleId,
                   @RequestBody RoomOperationPolicyScheduleUpdateDto scheduleUpdateDto) {

        RoomOperationPolicySchedule updatedSchedule
                = scheduleService.updateSchedule(roomOperationPolicyScheduleId, scheduleUpdateDto);

        ScheduleResponseDto updatedScheduleDto
                = scheduleService.dtoFrom(updatedSchedule);

        return new ResponseEntity<>(updatedScheduleDto, HttpStatus.OK);
    }

    @Operation(summary = "✅[관리자] schedule 삭제",
            description = "해당 schedule id의 정보 삭제 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @DeleteMapping("/{roomOperationPolicyScheduleId}")
     ResponseEntity<ApiResponseDto<Void>> deleteSchedule(@PathVariable Long roomOperationPolicyScheduleId) {
        scheduleService.deleteScheduleById(roomOperationPolicyScheduleId);
        ApiResponseDto<Void> response
                = new ApiResponseDto<>(HttpStatus.NO_CONTENT.toString(), "정상적으로 삭제 되었습니다.", null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}


