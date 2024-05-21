package com.test.studyroomreservationsystem.apicontroller.any;

import com.test.studyroomreservationsystem.dto.room.RoomsResponseDto;
import com.test.studyroomreservationsystem.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
public class AnyReservationController {

//    @Slf4j
    @Tag(name = "Reservation", description = "예약 정보 관련 API")
    @RestController
    @RequestMapping("/reservations")
    public class ReservationController {
        private final RoomService roomService;

        @Autowired
        public ReservationController(RoomService roomService) {
            this.roomService = roomService;
        }
        // todo 수정 예정

        @Operation(summary = "✅ 해당 날짜 모든룸 예약 상태 확인 ",
                description = "날짜를 받으면 모든 룸의 예약을 확인",
                security = {})
        @GetMapping("/by-date")
        ResponseEntity<List<RoomsResponseDto>> getRoomReservationsByDate(@RequestParam("date") LocalDate date) {
            List<RoomsResponseDto> responseDtoList = roomService.getRoomsReservationsByDate(date);

            return new ResponseEntity<>(responseDtoList, HttpStatus.OK);
        }
    }

}
