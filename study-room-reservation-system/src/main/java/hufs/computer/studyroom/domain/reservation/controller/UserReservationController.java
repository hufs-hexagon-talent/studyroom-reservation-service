package hufs.computer.studyroom.domain.reservation.controller;

import hufs.computer.studyroom.common.response.SuccessResponse;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import hufs.computer.studyroom.domain.reservation.dto.request.CreateReservationRequest;
import hufs.computer.studyroom.domain.reservation.dto.response.ReservationInfoResponse;
import hufs.computer.studyroom.domain.reservation.dto.response.ReservationInfoResponses;
import hufs.computer.studyroom.domain.reservation.dto.response.UserNoShowCntResponse;
import hufs.computer.studyroom.domain.reservation.service.ReservationCommandService;
import hufs.computer.studyroom.domain.reservation.service.ReservationQueryService;
import hufs.computer.studyroom.domain.user.entity.User;
import hufs.computer.studyroom.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Tag(name = "Reservation", description = "예약 정보 관련 API")
@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class UserReservationController {
    private final ReservationCommandService reservationCommandService;
    private final ReservationQueryService reservationQueryService;

    @Operation(summary = "✅ 자신의 예약 생성",
            description = "인증 받은 유저 사용자 예약 생성",
            security = {@SecurityRequirement(name = "JWT")})
    @PostMapping
    ResponseEntity<SuccessResponse<ReservationInfoResponse>> reserveProcess(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                                            @RequestBody CreateReservationRequest request) {
        User user = currentUser.getUser();
        var result = reservationCommandService.createReservation(request, user);
        return ResponseFactory.created(result);
    }

    @Operation(summary = "✅ 자신의 예약 삭제",
            description = "인증 받은 유저의 자신의 예약 삭제",
            security = {@SecurityRequirement(name = "JWT")})
    @DeleteMapping("/me/{reservationId}")
    public ResponseEntity<SuccessResponse<Void>> deleteReservationBySelf(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                                           @PathVariable Long reservationId) {
        reservationCommandService.deleteReservationBySelf(reservationId, currentUser);
        return ResponseFactory.deleted();
    }

//    -------------------------------------------------------------------------------------------------------------
//   todo : createAt (예약생성 시간)기준으로 가져올지 vs reservationStartTime ( 예약 시작 시간 ) 기준으로 가져올지
//    @Operation(summary = "✅ 자신의 최근 예약 조회",
//            description = " 인증 받은 유저의 자신의 최근(현재) 예약 조회 ",
//            security = {@SecurityRequirement(name = "JWT")})
//    @GetMapping("/me/latest")
//    ResponseEntity<SuccessResponse<ReservationInfoResponse>> lookUpRecent(@AuthenticationPrincipal CustomUserDetails currentUser) {
//        var result = reservationQueryService.findRecentReservationByUserId(currentUser.getUser().getUserId());
//        return ResponseFactory.success(result);
//    }


    @Operation(summary = "✅ 자신의 모든 예약 기록 조회 ",
            description = " 인증 받은 유저 자신의 모든 예약 조회",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/me")
    ResponseEntity<SuccessResponse<ReservationInfoResponses>> lookUpAllHistory(@AuthenticationPrincipal CustomUserDetails currentUser) {

        var result = reservationQueryService.findAllReservationByUser(currentUser);

        return ResponseFactory.success(result);
    }


    @Operation(summary = "✅ 자신의 NoShow 정보 조회",
            description = " 인증 받은 유저의 자신의 노쇼 횟수 조회 ",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/me/no-show")
    ResponseEntity<SuccessResponse<UserNoShowCntResponse>> lookUpNoShowCount(@AuthenticationPrincipal CustomUserDetails currentUser) {
        var result = reservationQueryService.getNoShowReservations(currentUser);
        return ResponseFactory.success(result);
    }
}
