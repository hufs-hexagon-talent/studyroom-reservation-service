package hufs.computer.studyroom.domain.reservation.controller;

import hufs.computer.studyroom.common.response.SuccessResponse;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import hufs.computer.studyroom.common.validation.annotation.ExistReservation;
import hufs.computer.studyroom.common.validation.annotation.user.ExistUser;
import hufs.computer.studyroom.domain.reservation.dto.request.ModifyReservationStateRequest;
import hufs.computer.studyroom.domain.reservation.dto.response.BlockedUserNoShowResponses;
import hufs.computer.studyroom.domain.reservation.dto.response.ReservationInfoResponse;
import hufs.computer.studyroom.domain.reservation.dto.response.ReservationInfoResponses;
import hufs.computer.studyroom.domain.reservation.dto.response.ReservationStaticResponse;
import hufs.computer.studyroom.domain.reservation.service.ReservationCommandService;
import hufs.computer.studyroom.domain.reservation.service.ReservationQueryService;
import hufs.computer.studyroom.domain.auth.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
    @Operation(summary = "❌[관리자] 특정 날짜 + 특정 partition 들에 대한 모든 예약 상태 확인 ",
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

        var result = reservationQueryService.getReservationStatics(date);

        return ResponseFactory.success(result);
    }


}
