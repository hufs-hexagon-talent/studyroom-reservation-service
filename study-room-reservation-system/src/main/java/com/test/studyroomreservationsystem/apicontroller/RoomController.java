package com.test.studyroomreservationsystem.apicontroller;

import com.test.studyroomreservationsystem.dto.room.RoomsReservationRequestDto;
import com.test.studyroomreservationsystem.dto.room.RoomsReservationResponseDto;
import com.test.studyroomreservationsystem.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Room", description = "방 정보 관련 API")
@RestController
@Slf4j
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService roomService;
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    @Operation(summary = "✅해당 날짜 모든룸 예약 상태 확인 ", description = "")
    ResponseEntity<List<RoomsReservationResponseDto>> getRoomReservationsByDate(@RequestBody RoomsReservationRequestDto roomsRequestDto) {
        LocalDate date = roomsRequestDto.getDate();
        List<RoomsReservationResponseDto> reservationsResponseDto = roomService.getRoomReservationsByDate(date);

        return new ResponseEntity<>(reservationsResponseDto, HttpStatus.OK);
    }
}
