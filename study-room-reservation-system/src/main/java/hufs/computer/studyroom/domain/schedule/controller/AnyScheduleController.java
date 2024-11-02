package hufs.computer.studyroom.domain.schedule.controller;

import hufs.computer.studyroom.common.response.SuccessResponse;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import hufs.computer.studyroom.domain.schedule.dto.response.AvailableDateResponses;
import hufs.computer.studyroom.domain.schedule.service.ScheduleQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "PolicySchedule", description = "날짜에 따른 방운영 정책")
@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class AnyScheduleController {
    private final ScheduleQueryService scheduleQueryService;
    @Operation(summary = "🚧 현재로 부터 미래까지 운영 정책이 설정된 방이 있는 날짜를 조회",
            description = "현재로 부터 예약가능한 방들을 날짜를 기준으로 묶어 조회"
    )
    @GetMapping("/available-dates/{departmentId}")
    public ResponseEntity<SuccessResponse<AvailableDateResponses>> getAvailableDatesFromToday(@PathVariable Long departmentId) {
        var result = scheduleQueryService.getAvailableDatesFromToday(departmentId);
        return ResponseFactory.success(result);
    }
}
