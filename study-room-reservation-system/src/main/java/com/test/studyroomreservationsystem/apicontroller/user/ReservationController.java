package com.test.studyroomreservationsystem.apicontroller.user;

import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.dto.user.UserNoShowCntResponseDto;
import com.test.studyroomreservationsystem.dto.util.ApiResponseDto;
import com.test.studyroomreservationsystem.dto.util.ApiResponseListDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationRequestDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationInfoResponseDto;
import com.test.studyroomreservationsystem.security.CustomUserDetails;
import com.test.studyroomreservationsystem.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Tag(name = "Reservation", description = "예약 정보 관련 API")
@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "✅ 자신의 예약 생성",
            description = "인증 받은 유저 사용자 예약 생성",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PostMapping
    ResponseEntity<ApiResponseDto<ReservationInfoResponseDto>> reserveProcess(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                                              @RequestBody ReservationRequestDto reservationRequestDto) {
        Reservation createdReservation = reservationService.createReservation(reservationRequestDto, currentUser.getUser());
        ReservationInfoResponseDto reservation = reservationService.responseDtoFrom(createdReservation);
        ApiResponseDto<ReservationInfoResponseDto> response
                = new ApiResponseDto<>(HttpStatus.CREATED.toString(), "정상적으로 생성 되었습니다.", reservation);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @Operation(summary = "✅ 자신의 최근 예약 조회",
            description = " 인증 받은 유저의 자신의 최근(현재) 예약 조회 ",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping("/me/latest")
    ResponseEntity<ApiResponseDto<ReservationInfoResponseDto>> lookUpRecent(@AuthenticationPrincipal CustomUserDetails currentUser) {
        Reservation recentReservation = reservationService.findRecentReservationByUserId(currentUser.getUser().getUserId());
        ReservationInfoResponseDto reservationDto = reservationService.responseDtoFrom(recentReservation);
        ApiResponseDto<ReservationInfoResponseDto> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", reservationDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "✅ 자신의 모든 예약 기록 조회 ",
            description = " 인증 받은 유저 자신의 모든 예약 조회",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping("/me")
    ResponseEntity<ApiResponseDto<ApiResponseListDto<ReservationInfoResponseDto>>> lookUpAllHistory(@AuthenticationPrincipal CustomUserDetails currentUser) {

        List<ReservationInfoResponseDto> reservationsByUser = reservationService.findAllReservationByUser(currentUser.getUser().getUserId())
                .stream()
                .map(reservationService::responseDtoFrom)
                .toList();

        ApiResponseListDto<ReservationInfoResponseDto> wrapped
                = new ApiResponseListDto<>(reservationsByUser);
        ApiResponseDto<ApiResponseListDto<ReservationInfoResponseDto>> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", wrapped);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "✅ 자신의 예약 삭제",
            description = "인증 받은 유저의 자신의 예약 삭제",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @DeleteMapping("/me/{reservationId}")
    public ResponseEntity<ApiResponseDto<Objects>> deleteReservationBySelf(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                                           @PathVariable Long reservationId) {
        reservationService.deleteReservationBySelf(reservationId, currentUser);
        ApiResponseDto<Objects> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 삭제 되었습니다.", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "✅ 자신의 NoShow 정보 조회",
            description = " 인증 받은 유저의 자신의 노쇼 횟수 조회 ",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping("/me/no-show")
    ResponseEntity<ApiResponseDto<UserNoShowCntResponseDto>> lookUpNoShowCount(@AuthenticationPrincipal CustomUserDetails currentUser) {
        Long userId = currentUser.getUser().getUserId();
        List<Reservation> reservations = reservationService.getNoShowReservations(userId);
        List<ReservationInfoResponseDto> reservationInfos
                = reservations.stream().map(reservationService::responseDtoFrom).toList();

        int count = reservations.size();
        UserNoShowCntResponseDto userNoShowCntDto = new UserNoShowCntResponseDto(count,reservationInfos);
        ApiResponseDto<UserNoShowCntResponseDto> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", userNoShowCntDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
