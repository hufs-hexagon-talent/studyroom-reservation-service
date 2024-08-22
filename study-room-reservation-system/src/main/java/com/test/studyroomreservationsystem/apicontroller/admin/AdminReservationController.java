package com.test.studyroomreservationsystem.apicontroller.admin;

import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.dto.reservation.ReservationInfoResponseDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationInfoUpdateRequestDto;
import com.test.studyroomreservationsystem.dto.user.UserInfoUpdateRequestDto;
import com.test.studyroomreservationsystem.dto.util.ApiResponseDto;
import com.test.studyroomreservationsystem.dto.util.ApiResponseListDto;
import com.test.studyroomreservationsystem.security.CustomUserDetails;
import com.test.studyroomreservationsystem.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@Tag(name = "Reservation", description = "예약 정보 관련 API")
@RestController
@RequestMapping("/reservations")
public class AdminReservationController {
    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "✅[관리자] 사용자 예약 삭제",
            description = "관리용 예약 삭제",
            security = {@SecurityRequirement(name = "JWT")}
    )

    @DeleteMapping("/admin/{reservationId}")
    public ResponseEntity<ApiResponseDto<Objects>> deleteReservation(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                                            @PathVariable Long reservationId) {

        reservationService.deleteReservationByAdmin(reservationId,currentUser);
        ApiResponseDto<Objects> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 삭제 되었습니다.", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "✅[관리자] 학번으로 사용자 예약들 조회",
            description = "관리용 예약 조회",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping("/admin/{serial}")
    public ResponseEntity<ApiResponseDto<ApiResponseListDto<ReservationInfoResponseDto>>> getReservationBySerial(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                                                                                 @PathVariable String serial) {
        List<ReservationInfoResponseDto> responseDtoList = reservationService.findAllReservationByAdmin(serial, currentUser)
                .stream()
                .map(reservationService::responseDtoFrom)
                .toList();
        ApiResponseListDto<ReservationInfoResponseDto> wrapped = new ApiResponseListDto<>(responseDtoList);
        ApiResponseDto<ApiResponseListDto<ReservationInfoResponseDto>> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", wrapped);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Operation(summary = "✅[관리자] 특정 예약 상태 변경",
            description = "관리용 예약 수정",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PatchMapping("/admin/{reservationId}")
    public ResponseEntity<ApiResponseDto<ReservationInfoResponseDto>> updateReservationInfoByAdmin(@PathVariable Long reservationId,
                                                                                                   @RequestBody ReservationInfoUpdateRequestDto requestDto) {
        Reservation reservation = reservationService.updateReservationInfo(reservationId, requestDto);
        ReservationInfoResponseDto reservationInfoDto = reservationService.responseDtoFrom(reservation);

        ApiResponseDto<ReservationInfoResponseDto> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 수정 되었습니다.", reservationInfoDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
