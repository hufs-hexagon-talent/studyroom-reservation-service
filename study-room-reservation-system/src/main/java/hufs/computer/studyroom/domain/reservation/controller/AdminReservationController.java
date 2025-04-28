package hufs.computer.studyroom.domain.reservation.controller;

import hufs.computer.studyroom.common.response.SuccessResponse;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import hufs.computer.studyroom.common.util.excel.core.ExcelFile;
import hufs.computer.studyroom.common.util.excel.core.OneSheetExcelFile;
import hufs.computer.studyroom.common.validation.annotation.ExistReservation;
import hufs.computer.studyroom.common.validation.annotation.user.ExistUser;
import hufs.computer.studyroom.domain.reservation.dto.excel.ReservationExportExcelDto;
import hufs.computer.studyroom.domain.reservation.dto.request.ModifyReservationStateRequest;
import hufs.computer.studyroom.domain.reservation.dto.response.BlockedUserNoShowResponses;
import hufs.computer.studyroom.domain.reservation.dto.response.ReservationInfoResponse;
import hufs.computer.studyroom.domain.reservation.dto.response.ReservationInfoResponses;
import hufs.computer.studyroom.domain.reservation.dto.response.ReservationStaticResponse;
import hufs.computer.studyroom.domain.reservation.entity.Reservation;
import hufs.computer.studyroom.domain.reservation.service.ReservationCommandService;
import hufs.computer.studyroom.domain.reservation.service.ReservationQueryService;
import hufs.computer.studyroom.domain.auth.security.CustomUserDetails;
import hufs.computer.studyroom.domain.user.dto.excel.UserExportExcelDto;
import hufs.computer.studyroom.domain.user.entity.ServiceRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static hufs.computer.studyroom.domain.reservation.entity.Reservation.*;
import static java.time.temporal.WeekFields.ISO;

@Slf4j
@Tag(name = "Reservation", description = "예약 정보 관련 API")
@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Validated
public class AdminReservationController {
    private final ReservationCommandService reservationCommandService;
    private final ReservationQueryService reservationQueryService;

    @Operation(summary = "✅[관리자] 사용자 예약 삭제",
            description = "관리용 예약 삭제",
            security = {@SecurityRequirement(name = "JWT")})
    @DeleteMapping("/admin/{reservationId}")
    public ResponseEntity<SuccessResponse<Void>> deleteReservation(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                                      @ExistReservation @PathVariable Long reservationId) {

        reservationCommandService.deleteReservationByAdmin(reservationId,currentUser);

        return ResponseFactory.deleted();
    }

    @Operation(summary = "✅[관리자] 특정 예약 상태 변경",
            description = "관리용 예약 수정",
            security = {@SecurityRequirement(name = "JWT")})
    @PatchMapping("/admin/{reservationId}")
    public ResponseEntity<SuccessResponse<ReservationInfoResponse>> updateReservationInfoByAdmin(@ExistReservation @PathVariable Long reservationId,
                                                                                                 @Valid @RequestBody ModifyReservationStateRequest request) {
        var result = reservationCommandService.updateReservationState(reservationId, request);

        return ResponseFactory.modified(result);
    }

    @Operation(summary = "✅[관리자] userId로 사용자의 예약들 조회",
            description = "관리용 예약 조회",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/admin/users/{userId}")
    public ResponseEntity<SuccessResponse<ReservationInfoResponses>> getReservationByUserId(@ExistUser @PathVariable Long userId) {
// todo : [의논] 어느 시점까지의 예약을 가져와야할까? 시간이 가면 갈 수 록, 예약이 너무 많아 질텐데,,,
        var result = reservationQueryService.findAllReservationByUser(userId);

        return ResponseFactory.success(result);
    }

    @Operation(summary = "✅[관리자] 블락 당한 사용자들의 예약들 조회",
            description = "관리용 예약 조회",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/admin/blocked/users")
    public ResponseEntity<SuccessResponse<BlockedUserNoShowResponses>> getBlockedUserReservationInfo() {
//        todo : 관리자 검증 애노테이션 생성
        var result = reservationQueryService.getBlockedUserReservation();

        return ResponseFactory.success(result);
    }

//todo : 추후 신에게 검토
    @Operation(summary = "✅[관리자] 특정 날짜 + 특정 partition 들에 대한 모든 예약 상태 확인 ",
            description = "파티션 별 로 예약 관리를 위해 날짜와 특정 파티션들에 대한 모든 예약을 확인",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/partitions/by-date")
    public ResponseEntity<SuccessResponse<ReservationInfoResponses>> getReservationsByPartitionsAndDate(
            @RequestParam("date") LocalDate date,
            @RequestParam("partitionIds") List<Long> partitionIds) {

                var result = reservationQueryService.getReservationsByPartitionsAndDate(partitionIds, date);

        return ResponseFactory.success(result);
    }

    @Operation(summary = "✅[관리자] 금일 예약들 통계 조회",
            description = "관리용 예약 수치 조회",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/admin/statics/by-date")
    public ResponseEntity<SuccessResponse<ReservationStaticResponse>> getReservationStatics(
            @RequestParam("date") LocalDate date) {

        var result = reservationQueryService.getReservationStaticsByDate(date);

        return ResponseFactory.success(result);
    }

    @Operation(summary = "🚧 [관리자] 예약 정보 Excel 내보내기",
            description = "예약 정보 Excel 추출",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/export/excel")
    public ResponseEntity<SuccessResponse<Void>> exportExcel(

            @Parameter(description = "필터링 할 상태 목록 (없으면 전체)")
            @RequestParam(value = "states", required = false)
            List<ReservationState> states,

            @Parameter(description = "조회 시작 시각(ISO-8601, UTC) 예: 2025-04-01T00:00:00Z")
            @RequestParam(value = "startDateTime", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDateTime,

            @Parameter(description = "조회 종료 시각(ISO-8601, UTC)")
            @RequestParam(value = "endDateTime", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDateTime,

            HttpServletResponse response) throws IOException {


        List<ReservationExportExcelDto> data = reservationQueryService.getExcelDTOs(states, startDateTime, endDateTime);
        ExcelFile excelFile = new OneSheetExcelFile<>(data, ReservationExportExcelDto.class);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=reservations.xlsx");

        excelFile.write(response.getOutputStream());

        return ResponseFactory.success(null);
    }

}
