package com.test.studyroomreservationsystem.apicontroller.any;

import com.test.studyroomreservationsystem.dto.util.ApiResponseDto;
import com.test.studyroomreservationsystem.dto.util.ApiResponseListDto;
import com.test.studyroomreservationsystem.service.impl.RoomOperationPolicyScheduleServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
@Tag(name = "PolicySchedule", description = "날짜에 따른 방운영 정책")
@RestController
@RequestMapping("/schedules")
public class AnyScheduleController {
    private final RoomOperationPolicyScheduleServiceImpl scheduleService;
    public AnyScheduleController(RoomOperationPolicyScheduleServiceImpl scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Operation(summary = "✅ 현재로 부터 미래까지 운영 정책이 설정된 방이 있는 날짜를 조회",
            description = "현재로 부터 예약가능한 방들을 날짜를 기준으로 묶어 조회"
    )
    @GetMapping("/available-dates")
    public ResponseEntity<ApiResponseDto<ApiResponseListDto<LocalDate>>> getAvailableDatesFromToday() {
        List<LocalDate> availableDates = scheduleService.getAvailableDatesFromToday();

        ApiResponseListDto<LocalDate> wrapped = new ApiResponseListDto<>(availableDates);
        ApiResponseDto<ApiResponseListDto<LocalDate>> response = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", wrapped);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
