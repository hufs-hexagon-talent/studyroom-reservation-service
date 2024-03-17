package com.test.studyroomreservationsystem.apicontroller;

import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.repository.ReservationRepository;
import com.test.studyroomreservationsystem.dto.reservation.ReservationDto;
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
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "reservation 생성", description = "reservation 생성하는 API")
    @PostMapping
    ResponseEntity<ReservationDto> createReservation(@RequestBody ReservationDto reservationDto) {
        Reservation createdReservation = reservationService.createReservation(reservationDto);
        ReservationDto reservation = reservationService.convertToDto(createdReservation);

        return new ResponseEntity<>(reservation, HttpStatus.CREATED);
    }
    @Operation(summary = "reservation 조회", description = "reservation id로 조회 API")
    @GetMapping("/{reservationId}")
    ResponseEntity<ReservationDto> getReservationById(@PathVariable Long reservationId) {
        Reservation foundReservation = reservationService.findReservationById(reservationId);
        ReservationDto reservation = reservationService.convertToDto(foundReservation);

        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }
    @Operation(summary = "모든 reservation 조회", description = "모든 reservation 조회 API")
    @GetMapping
    ResponseEntity<List<ReservationDto>> getAllReservations() {
        List<ReservationDto> reservations = reservationService.findAllReservation()
                .stream()
                .map(reservationService::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }
    @Operation(summary = "reservation 정보 업데이트", description = "해당 reservation id의 정보 업데이트 API")
    @PutMapping("/{reservationId}")
    ResponseEntity<ReservationDto> updateReservation(@PathVariable Long reservationId,
                                                     @RequestBody ReservationDto reservationDto) {
        Reservation updateReservation = reservationService.updateReservation(reservationId, reservationDto);
        ReservationDto reservation = reservationService.convertToDto(updateReservation);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }
    @Operation(summary = "reservation 삭제", description = "해당 reservation id의 정보 삭제 API")
    @DeleteMapping("/{reservationId}")
    ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        reservationService.deleteReservationById(reservationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
