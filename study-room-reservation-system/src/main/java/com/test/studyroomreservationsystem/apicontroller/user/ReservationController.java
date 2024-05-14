package com.test.studyroomreservationsystem.apicontroller.user;

import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.dto.reservation.RequestReservationDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationRoomDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationTimeDto;
import com.test.studyroomreservationsystem.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Reservation", description = "예약 정보 관련 API")
@RestController
@RequestMapping("/users/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    // todo 수정 예정
//    @Operation(summary = "\uD83D\uDEA7 예약 생성",
//            description = "인증 받은 유저 사용자 예약 생성",
//            security = {@SecurityRequirement(name = "JWT")}
//    )
//    @PostMapping
//    ResponseEntity<ReservationDto> reserveProcess(@RequestBody ReservationDto reservationDto) {
//        Reservation createdReservation = reservationService.createReservation(reservationDto);
//        ReservationDto reservation = reservationService.dtoFrom(createdReservation);
//
//        return new ResponseEntity<>(reservation, HttpStatus.CREATED);
//    }
    // todo 수정 예정
    @Operation(summary = "❌ 예약 조회",
            description = " 인증 받은 유저 자신의 현재 예약 조회",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping("/{reservationId}")
    ResponseEntity<RequestReservationDto> lookUpRecent(@PathVariable Long reservationId) {
        Reservation foundReservation = reservationService.findReservationById(reservationId);
        RequestReservationDto reservation = reservationService.dtoFrom(foundReservation);

        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }
    // todo 수정 예정
    @Operation(summary = "❌ 모든 예약 기록 조회 ",
            description = " 인증 받은 유저 자신의 모든 예약 조회",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @GetMapping("/user-history/{userId}") // URI 재구성
    ResponseEntity<List<RequestReservationDto>> lookUpAllHistory(@PathVariable Long userId) {

        List<RequestReservationDto> reservationsByUser = reservationService.findAllReservationByUser(userId)
                .stream()
                .map(reservationService::dtoFrom)
                .collect(Collectors.toList());
        return new ResponseEntity<>(reservationsByUser, HttpStatus.OK);
    }
    // todo 수정 예정
    @Operation(summary = "❌ 예약 수정 방 변경",
            description = " 본인이 예약한 정보(방)을 수정",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PutMapping("/{reservationId}/room")
    ResponseEntity<RequestReservationDto> editReservationByRoom(@PathVariable Long reservationId,
                                                                @RequestBody ReservationRoomDto reservationDto) {
        Reservation updateReservation = reservationService.updateRoomReservation(reservationId, reservationDto);
        RequestReservationDto reservation = reservationService.dtoFrom(updateReservation);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }
    // todo 수정 예정
    @Operation(summary = "❌ 예약 수정 시간 업데이트",
            description = "본인이 예약한 정보(시간)을 수정",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PutMapping("/{reservationId}/time")
    ResponseEntity<RequestReservationDto> editReservationByTime(@PathVariable Long reservationId,
                                                                @RequestBody ReservationTimeDto reservationDto) {
        Reservation updateReservation = reservationService.updateTimeReservation(reservationId, reservationDto);
        RequestReservationDto reservation = reservationService.dtoFrom(updateReservation);
                return new ResponseEntity<>(reservation, HttpStatus.OK);
    }
    //    메인 API → 날짜 주면, 각 방에서 어떤 예약들이 있는지 (전체 방에 대해서)

}
