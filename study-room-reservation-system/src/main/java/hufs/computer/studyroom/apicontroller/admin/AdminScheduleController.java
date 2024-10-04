package hufs.computer.studyroom.apicontroller.admin;

import hufs.computer.studyroom.domain.entity.RoomOperationPolicySchedule;
import hufs.computer.studyroom.dto.util.ApiResponseDto;
import hufs.computer.studyroom.dto.operationpolicyschedule.ScheduleRequestDto;
import hufs.computer.studyroom.dto.operationpolicyschedule.ScheduleResponseDto;
import hufs.computer.studyroom.dto.operationpolicyschedule.RoomOperationPolicyScheduleUpdateDto;
import hufs.computer.studyroom.dto.util.ApiResponseListDto;
import hufs.computer.studyroom.service.impl.RoomOperationPolicyScheduleServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "PolicySchedule", description = "날짜에 따른 방운영 정책")
@RestController
@RequestMapping("/schedules")
public class AdminScheduleController {
    private final RoomOperationPolicyScheduleServiceImpl scheduleService;

    @Autowired
    public AdminScheduleController(RoomOperationPolicyScheduleServiceImpl scheduleService) {
        this.scheduleService = scheduleService;
    }

    // todo : 트랜잭션
    @Operation(summary = "✅[관리자] 여러 schedule 생성",
            description = "여러 스케쥴 생성/ 여러개의 방에 여러 날짜에 스케쥴 생성 ",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PostMapping()
     ResponseEntity<ApiResponseDto<ApiResponseListDto<ScheduleResponseDto>>> createSchedules(@RequestBody ScheduleRequestDto requestDto) {

        List<RoomOperationPolicySchedule> createdSchedules
                = scheduleService.createSchedules(requestDto);

        List<ScheduleResponseDto> createdSchedulesDto = createdSchedules.stream()
                .map(scheduleService::dtoFrom)
                .toList();

        ApiResponseListDto<ScheduleResponseDto> wrapped = new ApiResponseListDto<>(createdSchedulesDto);

        ApiResponseDto<ApiResponseListDto<ScheduleResponseDto>> response
                = new ApiResponseDto<>(HttpStatus.CREATED.toString(), "정상적으로 생성 되었습니다.", wrapped);

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

    @Operation(summary = "✅[관리자] 해당날짜의 schedule 들 조회",
            description = "스케쥴 조회",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping("date/{policyApplicationDate}")
    ResponseEntity<ApiResponseDto<ApiResponseListDto<ScheduleResponseDto>>> getSchedules(@PathVariable LocalDate policyApplicationDate) {
        List<RoomOperationPolicySchedule> schedules = scheduleService.findScheduleByDate(policyApplicationDate);
        List<ScheduleResponseDto> schedulesDto = schedules.stream()
                .map(scheduleService::dtoFrom)
                .toList();
        ApiResponseListDto<ScheduleResponseDto> wrapped = new ApiResponseListDto<>(schedulesDto);

        ApiResponseDto<ApiResponseListDto<ScheduleResponseDto>> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", wrapped);

        return new ResponseEntity<>(response, HttpStatus.OK);
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
     ResponseEntity<ApiResponseDto<Object>> deleteSchedule(@PathVariable Long roomOperationPolicyScheduleId) {
        scheduleService.deleteScheduleById(roomOperationPolicyScheduleId);
        ApiResponseDto<Object> response = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 삭제 되었습니다.", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }
}


