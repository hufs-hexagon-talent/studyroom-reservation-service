package com.test.studyroomreservationsystem.apicontroller.any;

import com.test.studyroomreservationsystem.dto.reservation.SpecificRoomsReservationsDto;
import com.test.studyroomreservationsystem.dto.util.ApiResponseDto;
import com.test.studyroomreservationsystem.dto.util.ApiResponseListDto;
import com.test.studyroomreservationsystem.dto.reservation.PartitionsReservationResponseDto;
import com.test.studyroomreservationsystem.service.ReservationService;
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

        private final ReservationService reservationService;

        @Autowired
        public AnyReservationController(ReservationService reservationService) {
            this.reservationService = reservationService;
        }
        @Operation(summary = "🚧 해당 날짜 모든 파티션 예약 상태 확인 ",
                description = "날짜를 받으면 모든 파티션의 예약을 확인, 예약 현황 테이블을 그릴때 사용",
                security = {})
        @GetMapping("/by-date")
        ResponseEntity<ApiResponseDto<ApiResponseListDto<PartitionsReservationResponseDto>>> getPartitionReservationsByDate(@RequestParam("date") LocalDate date) {
            List<PartitionsReservationResponseDto> responseDtoList
                    = reservationService.getReservationsByAllPartitionsAndDate(date);

            ApiResponseListDto<PartitionsReservationResponseDto> wrapped
                    = new ApiResponseListDto<>(responseDtoList);

            ApiResponseDto<ApiResponseListDto<PartitionsReservationResponseDto>> response
                    = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", wrapped);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @Operation(summary = "🚧 특정 날짜, 특정 room 들 모든 예약 상태 확인 ",
            description = "날짜를 받으면 특정 룸들의 예약을 확인",
            security = {})
        @GetMapping("/partitions/by-date")
        public ResponseEntity<ApiResponseDto<SpecificRoomsReservationsDto>> getReservationsByPartitionsByDate(
                @RequestParam("date") LocalDate date,
                @RequestParam("partitionIds") List<Long> partitionIds) {

            SpecificRoomsReservationsDto responseDto
                    = reservationService.getReservationsByPartitionsAndDate(partitionIds, date);

            ApiResponseDto<SpecificRoomsReservationsDto> response
                    = new ApiResponseDto<>(HttpStatus.OK.toString(), "정상적으로 조회 되었습니다.", responseDto);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }


