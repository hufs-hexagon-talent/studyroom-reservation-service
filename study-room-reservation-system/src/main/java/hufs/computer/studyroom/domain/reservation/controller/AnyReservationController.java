package hufs.computer.studyroom.domain.reservation.controller;

import hufs.computer.studyroom.common.response.SuccessResponse;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import hufs.computer.studyroom.common.validation.annotation.ExistDepartment;
import hufs.computer.studyroom.domain.reservation.dto.response.AllPartitionsReservationStatusResponse;
import hufs.computer.studyroom.domain.reservation.service.ReservationQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
@Tag(name = "Reservation", description = "예약 정보 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
@Validated
public class AnyReservationController {
        private final ReservationQueryService reservationQueryService;

        @Operation(summary = "❌ 특정 날짜 ,특정 부서가 관리하는 모든 파티션의 예약 상태 조회",
                description = "날짜를 받으면 룸(파티션 집합)들의 예약을 확인, 예약 현황 테이블을 그릴때 사용",
                security = {})
        @GetMapping("/by-date/{departmentId}")
        ResponseEntity<SuccessResponse<AllPartitionsReservationStatusResponse>> getPartitionReservationsByDepartmentAndDate(
                @ExistDepartment @PathVariable("departmentId") Long departmentId,
                @RequestParam("date") LocalDate date) {
            var result = reservationQueryService.getPartitionReservationsByDepartmentAndDate(departmentId, date);

            return ResponseFactory.success(result);
        }

    }


