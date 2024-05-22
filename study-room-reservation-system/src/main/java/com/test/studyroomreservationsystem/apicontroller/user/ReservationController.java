package com.test.studyroomreservationsystem.apicontroller.user;

import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.dto.ApiResponse;
import com.test.studyroomreservationsystem.dto.ApiResponseList;
import com.test.studyroomreservationsystem.dto.reservation.ReservationRequestDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationResponseDto;
import com.test.studyroomreservationsystem.dto.room.RoomsResponseDto;
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
    ResponseEntity<ApiResponse<ReservationRequestDto>> reserveProcess(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                         @RequestBody ReservationRequestDto reservationRequestDto) {
        Reservation createdReservation = reservationService.createReservation(reservationRequestDto, currentUser.getUser());
        ReservationRequestDto reservation = reservationService.requestDtoFrom(createdReservation);
        ApiResponse<ReservationRequestDto> response = new ApiResponse<>(HttpStatus.CREATED.toString(), "정상적으로 생성 되었습니다.", reservation);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @Operation(summary = "✅ 자신의 최근 예약 조회",
            description = " 인증 받은 유저의 자신의 최근(현재) 예약 조회 ",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping("/me/latest")
    ResponseEntity<ApiResponse<ReservationResponseDto>> lookUpRecent(@AuthenticationPrincipal CustomUserDetails currentUser) {
        Reservation recentReservation = reservationService.findRecentReservationByUserId(currentUser.getUser().getUserId());
        ReservationResponseDto reservationDto = reservationService.responseDtoFrom(recentReservation);
        ApiResponse<ReservationResponseDto> response = new ApiResponse<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", reservationDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // todo 수정 예정
    @Operation(summary = "✅ 자신의 모든 예약 기록 조회 ",
            description = " 인증 받은 유저 자신의 모든 예약 조회",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping("/me")
    ResponseEntity<ApiResponse<ApiResponseList<ReservationResponseDto>>> lookUpAllHistory(@AuthenticationPrincipal CustomUserDetails currentUser) {

        List<ReservationResponseDto> reservationsByUser = reservationService.findAllReservationByUser(currentUser.getUser().getUserId())
                .stream()
                .map(reservationService::responseDtoFrom)
                .collect(Collectors.toList());

        ApiResponseList<ReservationResponseDto> wrapped = new ApiResponseList<>(reservationsByUser);
        ApiResponse<ApiResponseList<ReservationResponseDto>> response = new ApiResponse<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", wrapped);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // todo 수정 예정
//    @Operation(summary = "❌ 예약 수정 방 변경",
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
//    // todo 수정 예정
//    @Operation(summary = "❌ 예약 수정 시간 업데이트",
//            description = "본인이 예약한 정보(시간)을 수정",
//            security = {@SecurityRequirement(name = "JWT")}
//    )
//    @PutMapping("/{reservationId}/time")
//    ResponseEntity<RequestReservationDto> editReservationByTime(@PathVariable Long reservationId,
//                                                                @RequestBody ReservationTimeDto reservationDto) {
//        Reservation updateReservation = reservationService.updateTimeReservation(reservationId, reservationDto);
//        RequestReservationDto reservation = reservationService.dtoFrom(updateReservation);
//                return new ResponseEntity<>(reservation, HttpStatus.OK);
//    }
    //    메인 API → 날짜 주면, 각 방에서 어떤 예약들이 있는지 (전체 방에 대해서)


        @Operation(summary = "✅ 해당 날짜 모든룸 예약 상태 확인 ",
                description = "날짜를 받으면 모든 룸의 예약을 확인",
                security = {})
        @GetMapping("/by-date")
        ResponseEntity<ApiResponse<ApiResponseList<RoomsResponseDto>>> getRoomReservationsByDate(@RequestParam("date") LocalDate date) {
            List<RoomsResponseDto> responseDtoList = roomService.getRoomsReservationsByDate(date);

            ApiResponseList<RoomsResponseDto> wrapped = new ApiResponseList<>(responseDtoList);
            ApiResponse<ApiResponseList<RoomsResponseDto>> response = new ApiResponse<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", wrapped);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
}
