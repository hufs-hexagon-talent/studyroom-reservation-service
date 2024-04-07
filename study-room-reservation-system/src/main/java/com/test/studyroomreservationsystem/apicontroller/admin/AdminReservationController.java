package com.test.studyroomreservationsystem.apicontroller.admin;

import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.dto.reservation.ReservationDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationRoomDto;
import com.test.studyroomreservationsystem.dto.reservation.ReservationStateDto;
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

//@Tag(name = "Admin", description = "[관리자]")
@Tag(name = "Reservation", description = "예약 정보 관련 API")
@RestController
@RequestMapping("/admin/reservations")
public class AdminReservationController {
    private final ReservationService reservationService;

    @Autowired
    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "reservation 생성", description = "reservation 생성 하는 API")
    @PostMapping
    ResponseEntity<ReservationDto> createReservation(@RequestBody ReservationDto reservationDto) {
        Reservation createdReservation = reservationService.createReservation(reservationDto);
        ReservationDto reservation = reservationService.dtoFrom(createdReservation);

        return new ResponseEntity<>(reservation, HttpStatus.CREATED);
    }
    @Operation(summary = "reservation 조회", description = "reservation id로 조회 API")
    @GetMapping("/{reservationId}")
    ResponseEntity<ReservationDto> getReservationById(@PathVariable Long reservationId) {
        Reservation foundReservation = reservationService.findReservationById(reservationId);
        ReservationDto reservation = reservationService.dtoFrom(foundReservation);

        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }
    @Operation(summary = "모든 reservation 조회", description = "모든 reservation 조회 API")
    @GetMapping
    ResponseEntity<List<ReservationDto>> getAllReservations() {
        List<ReservationDto> reservations = reservationService.findAllReservation()
                .stream()
                .map(reservationService::dtoFrom)
                .collect(Collectors.toList());
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @Operation(summary = "User 의 모든 Reservation 기록 조회 ", description = "user id로 reservation history 조회 API")
    @GetMapping("/user-history/{userId}") // URI 재구성
    ResponseEntity<List<ReservationDto>> getAllReservationsByUser(@PathVariable Long userId) {

        List<ReservationDto> reservationsByUser = reservationService.findAllReservationByUser(userId)
                .stream()
                .map(reservationService::dtoFrom)
                .collect(Collectors.toList());
        return new ResponseEntity<>(reservationsByUser, HttpStatus.OK);
    }

    @Operation(summary = "reservation 정보 업데이트", description = "해당 reservation id의 정보 업데이트 API")
    @PutMapping("/{reservationId}")
    ResponseEntity<ReservationDto> updateReservation(@PathVariable Long reservationId,
                                                     @RequestBody ReservationDto reservationDto) {
        Reservation updateReservation = reservationService.updateReservation(reservationId, reservationDto);
        ReservationDto reservation = reservationService.dtoFrom(updateReservation);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
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
    @Operation(summary = "reservation 상태 업데이트", description = "해당 reservation id의 상태 업데이트 API")
    @PutMapping("/{reservationId}/state")
    ResponseEntity<ReservationDto> updateStateReservation(@PathVariable Long reservationId,
                                                          @RequestBody ReservationStateDto reservationDto) {
        Reservation updateReservation = reservationService.updateStateReservation(reservationId, reservationDto);
        ReservationDto reservation = reservationService.dtoFrom(updateReservation);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @Operation(summary = "reservation 삭제", description = "해당 reservation id의 정보 삭제 API")
    @DeleteMapping("/{reservationId}")
    ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        reservationService.deleteReservationById(reservationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
