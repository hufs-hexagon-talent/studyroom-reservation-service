package com.test.studyroomreservationsystem.apicontroller.admin;

import com.test.studyroomreservationsystem.dto.util.ApiResponseDto;
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
}
