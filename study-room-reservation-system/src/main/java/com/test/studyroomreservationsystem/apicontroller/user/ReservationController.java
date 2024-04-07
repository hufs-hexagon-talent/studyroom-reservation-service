package com.test.studyroomreservationsystem.apicontroller.user;

import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.dto.reservation.ReservationDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationRoomDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationTimeDto;
import com.test.studyroomreservationsystem.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Reservation", description = "예약 정보 관련 API")
@RestController
@RequestMapping("/user/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "유저 본인의 reservation 생성", description = "유저 본인의 reservation 생성 하는 API")
    @PostMapping
    ResponseEntity<ReservationDto> createReservation(@RequestBody ReservationDto reservationDto) {
        Reservation createdReservation = reservationService.createReservation(reservationDto);
        ReservationDto reservation = reservationService.dtoFrom(createdReservation);

        return new ResponseEntity<>(reservation, HttpStatus.CREATED);
    }
    @Operation(summary = "유저 본인의 현재 reservation 조회", description = "유저 본인의 현재 reservation id로 조회 API")
    @GetMapping("/{reservationId}")
    ResponseEntity<ReservationDto> getReservationById(@PathVariable Long reservationId) {
        Reservation foundReservation = reservationService.findReservationById(reservationId);
        ReservationDto reservation = reservationService.dtoFrom(foundReservation);

        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @Operation(summary = "유저 본인의 모든 Reservation 기록 조회 ",description = "유저 본인 user id로 reservation history 조회 API")
    @GetMapping("/user-history/{userId}") // URI 재구성
    ResponseEntity<List<ReservationDto>> getAllReservationsByUser(@PathVariable Long userId) {

        List<ReservationDto> reservationsByUser = reservationService.findAllReservationByUser(userId)
                .stream()
                .map(reservationService::dtoFrom)
                .collect(Collectors.toList());
        return new ResponseEntity<>(reservationsByUser, HttpStatus.OK);
    }


    @Operation(summary = "reservation 룸 업데이트", description = "해당 reservation id의 룸 업데이트 API")
    @PutMapping("/{reservationId}/room")
    ResponseEntity<ReservationDto> updateRoomReservation(@PathVariable Long reservationId,
                                                     @RequestBody ReservationRoomDto reservationDto) {
        Reservation updateReservation = reservationService.updateRoomReservation(reservationId, reservationDto);
        ReservationDto reservation = reservationService.dtoFrom(updateReservation);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @Operation(summary = "reservation 시간 업데이트", description = "해당 reservation id의 시간 업데이트 API")
    @PutMapping("/{reservationId}/time")
    ResponseEntity<ReservationDto> updateTimeReservation(@PathVariable Long reservationId,
                                                     @RequestBody ReservationTimeDto reservationDto) {
        Reservation updateReservation = reservationService.updateTimeReservation(reservationId, reservationDto);
        ReservationDto reservation = reservationService.dtoFrom(updateReservation);
                return new ResponseEntity<>(reservation, HttpStatus.OK);
    }
    //    메인 API → 날짜 주면, 각 방에서 어떤 예약들이 있는지 (전체 방에 대해서)

}
