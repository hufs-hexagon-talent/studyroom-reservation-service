package com.test.studyroomreservationsystem.apicontroller.user;

import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.dto.ApiResponseDto;
import com.test.studyroomreservationsystem.dto.ApiResponseListDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationRequestDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationResponseDto;
import com.test.studyroomreservationsystem.dto.reservation.RoomsReservationResponseDto;
import com.test.studyroomreservationsystem.security.CustomUserDetails;
import com.test.studyroomreservationsystem.service.ReservationService;
import com.test.studyroomreservationsystem.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Tag(name = "Reservation", description = "예약 정보 관련 API")
@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final RoomService roomService;

    @Autowired
    public ReservationController(ReservationService reservationService, RoomService roomService) {
        this.reservationService = reservationService;
        this.roomService = roomService;
    }
    // todo 수정 예정

    @Operation(summary = "✅ 자신의 예약 생성",
            description = "인증 받은 유저 사용자 예약 생성",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PostMapping
    ResponseEntity<ApiResponseDto<ReservationResponseDto>> reserveProcess(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                                          @RequestBody ReservationRequestDto reservationRequestDto) {
        Reservation createdReservation = reservationService.createReservation(reservationRequestDto, currentUser.getUser());
        ReservationResponseDto reservation = reservationService.responseDtoFrom(createdReservation);
        ApiResponseDto<ReservationResponseDto> response
                = new ApiResponseDto<>(HttpStatus.CREATED.toString(), "정상적으로 생성 되었습니다.", reservation);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @Operation(summary = "✅ 자신의 최근 예약 조회",
            description = " 인증 받은 유저의 자신의 최근(현재) 예약 조회 ",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping("/me/latest")
    ResponseEntity<ApiResponseDto<ReservationResponseDto>> lookUpRecent(@AuthenticationPrincipal CustomUserDetails currentUser) {
        Reservation recentReservation = reservationService.findRecentReservationByUserId(currentUser.getUser().getUserId());
        ReservationResponseDto reservationDto = reservationService.responseDtoFrom(recentReservation);
        ApiResponseDto<ReservationResponseDto> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", reservationDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "✅ 자신의 모든 예약 기록 조회 ",
            description = " 인증 받은 유저 자신의 모든 예약 조회",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping("/me")
    ResponseEntity<ApiResponseDto<ApiResponseListDto<ReservationResponseDto>>> lookUpAllHistory(@AuthenticationPrincipal CustomUserDetails currentUser) {

        List<ReservationResponseDto> reservationsByUser = reservationService.findAllReservationByUser(currentUser.getUser().getUserId())
                .stream()
                .map(reservationService::responseDtoFrom)
                .collect(Collectors.toList());

        ApiResponseListDto<ReservationResponseDto> wrapped
                = new ApiResponseListDto<>(reservationsByUser);
        ApiResponseDto<ApiResponseListDto<ReservationResponseDto>> response
                = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", wrapped);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    // todo 수정 예정
    // 자신의 예약 삭제
    @Operation(summary = "✅ 자신의 예약 삭제",
            description = "인증 받은 유저의 자신의 예약 삭제",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @DeleteMapping("/me/{reservationId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteReservation(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                                  @PathVariable Long reservationId) {
        reservationService.deleteReservation(reservationId, currentUser);
        ApiResponseDto<Void> response
                = new ApiResponseDto<>(HttpStatus.NO_CONTENT.toString(), "정상적으로 삭제 되었습니다.", null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    //    메인 API → 날짜 주면, 각 방에서 어떤 예약들이 있는지 (전체 방에 대해서)
        @Operation(summary = "✅ 해당 날짜 모든룸 예약 상태 확인 ",
                description = "날짜를 받으면 모든 룸의 예약을 확인",
                security = {})
        @GetMapping("/by-date")
        ResponseEntity<ApiResponseDto<ApiResponseListDto<RoomsReservationResponseDto>>> getRoomReservationsByDate(@RequestParam("date") LocalDate date) {
            List<RoomsReservationResponseDto> responseDtoList = roomService.getRoomsReservationsByDate(date);

            ApiResponseListDto<RoomsReservationResponseDto> wrapped
                    = new ApiResponseListDto<>(responseDtoList);
            ApiResponseDto<ApiResponseListDto<RoomsReservationResponseDto>> response
                    = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", wrapped);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

}
    // todo 수정 예정
//    @Operation(summary = "❌ 예약 정보 변경",
//            description = " 본인이 예약한 정보(방)을 수정",
//            security = {@SecurityRequirement(name = "JWT")}
//    )
//    @PutMapping("/{reservationId}/room")
//    ResponseEntity<RequestReservationDto> editReservationByRoom(@PathVariable Long reservationId,
//                                                                @RequestBody ReservationRoomDto reservationDto) {
//        Reservation updateReservation = reservationService.updateRoomReservation(reservationId, reservationDto);
//        RequestReservationDto reservation = reservationService.dtoFrom(updateReservation);
//        return new ResponseEntity<>(reservation, HttpStatus.OK);
//    }
