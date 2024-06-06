package com.test.studyroomreservationsystem.apicontroller.any;

import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.dto.reservation.SpecificRoomsReservationsDto;
import com.test.studyroomreservationsystem.dto.util.ApiResponseDto;
import com.test.studyroomreservationsystem.dto.util.ApiResponseListDto;
import com.test.studyroomreservationsystem.dto.reservation.RoomsReservationResponseDto;
import com.test.studyroomreservationsystem.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@Tag(name = "Reservation", description = "예약 정보 관련 API")
@RestController
@RequestMapping("/reservations")
public class AnyReservationController {

        private final RoomService roomService;

        @Autowired
        public AnyReservationController(RoomService roomService) {
            this.roomService = roomService;
        }
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

        @Operation(summary = "✅ 특정 날짜, 특정 room 들 모든 예약 상태 확인 ",
            description = "날짜를 받으면 특정 룸들의 예약을 확인",
            security = {})
        @GetMapping("/rooms/by-date")
        public ResponseEntity<ApiResponseDto<SpecificRoomsReservationsDto>> getReservationsByRoomsByDate(
                @RequestParam("date") LocalDate date,
                @RequestParam("roomIds") List<Long> roomIds) {

            SpecificRoomsReservationsDto responseDto = roomService.getReservationsByRoomsAndDate(roomIds, date);

            ApiResponseDto<SpecificRoomsReservationsDto> response = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", responseDto);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }


