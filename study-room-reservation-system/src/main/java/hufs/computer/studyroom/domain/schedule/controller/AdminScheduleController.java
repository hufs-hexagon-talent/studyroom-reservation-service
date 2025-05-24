package hufs.computer.studyroom.domain.schedule.controller;

import hufs.computer.studyroom.common.response.SuccessResponse;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import hufs.computer.studyroom.common.validation.annotation.ExistSchedule;
import hufs.computer.studyroom.domain.schedule.dto.request.CreateScheduleBulkRequest;
import hufs.computer.studyroom.domain.schedule.dto.response.ScheduleInfoResponse;
import hufs.computer.studyroom.domain.schedule.dto.response.ScheduleInfoResponses;
import hufs.computer.studyroom.domain.schedule.dto.request.ModifyScheduleRequest;
import hufs.computer.studyroom.domain.schedule.dto.response.ScheduleTableResponse;
import hufs.computer.studyroom.domain.schedule.service.ScheduleCommandService;
import hufs.computer.studyroom.domain.schedule.service.ScheduleQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "PolicySchedule", description = "날짜에 따른 방운영 정책")
@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
@Validated
public class AdminScheduleController {
    private final ScheduleCommandService scheduleCommandService;
    private final ScheduleQueryService scheduleQueryService;


    // todo : 트랜잭션
    @Operation(summary = "✅[관리자] 여러 schedule 생성",
            description = "여러 스케쥴 생성/ 여러개의 방에 여러 날짜에 스케쥴 생성 ",
            security = {@SecurityRequirement(name = "JWT")})
    @PostMapping()
     ResponseEntity<SuccessResponse<ScheduleInfoResponses>> createSchedules(@RequestBody CreateScheduleBulkRequest request) {
        var result = scheduleCommandService.createSchedules(request);
        return ResponseFactory.created(result);
    }

    @Operation(summary = "✅[관리자] schedule 조회",
            description = "스케쥴 조회",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/{roomOperationPolicyScheduleId}")
     ResponseEntity<SuccessResponse<ScheduleInfoResponse>> getScheduleById(@ExistSchedule @PathVariable Long roomOperationPolicyScheduleId) {
        var result = scheduleQueryService.findScheduleById(roomOperationPolicyScheduleId);
        return ResponseFactory.success(result);
    }

    @Operation(summary = "✅[관리자] 해당날짜의 schedule 들 조회",
            description = "스케쥴 조회",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("date/{policyApplicationDate}")
    ResponseEntity<SuccessResponse<List<ScheduleTableResponse>>> getSchedules(@PathVariable LocalDate policyApplicationDate) {
        var result = scheduleQueryService.findScheduleByDate(policyApplicationDate);
        return ResponseFactory.success(result);
    }


    @Operation(summary = "✅[관리자] schedule 업데이트",
            description = "해당 스케쥴 업데이트",
            security = {@SecurityRequirement(name = "JWT")})
    @PutMapping("/schedule/{roomOperationPolicyScheduleId}")
    ResponseEntity<SuccessResponse<ScheduleInfoResponse>> modifySchedule(
            @ExistSchedule @PathVariable Long roomOperationPolicyScheduleId,
            @Valid @RequestBody ModifyScheduleRequest request) {

        var result = scheduleCommandService.updateSchedule(roomOperationPolicyScheduleId, request);
        return ResponseFactory.modified(result);

    }

    @Operation(summary = "✅[관리자] schedule 삭제",
            description = "해당 schedule id의 정보 삭제 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @DeleteMapping("/{roomOperationPolicyScheduleId}")
     ResponseEntity<SuccessResponse<Void>> deleteSchedule(
             @ExistSchedule @PathVariable Long roomOperationPolicyScheduleId) {
        scheduleCommandService.deleteScheduleById(roomOperationPolicyScheduleId);
        return ResponseFactory.deleted();
    }
}


