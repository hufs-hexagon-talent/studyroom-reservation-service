package com.test.studyroomreservationsystem.domain;

import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;
import com.test.studyroomreservationsystem.domain.repository.RoomOperationPolicyScheduleRepository;
import com.test.studyroomreservationsystem.exception.reservation.OperationClosedException;
import com.test.studyroomreservationsystem.exception.reservation.RoomPolicyNotFoundException;
import com.test.studyroomreservationsystem.service.DateTimeUtil;
import com.test.studyroomreservationsystem.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Slf4j
@SpringBootTest
public class isRoomAvailableTest {

    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomOperationPolicyScheduleRepository scheduleRepository;
//    현재 306은 운영시간은 2024-08-24T00:00:00Z ~ 2024-08-24T13:00:00Z 입니다.
//    예약하신 시간은 2024-08-24T04:00:00Z ~ 2024-08-24T05:00:00Z 입니다.
    @Test
    void check() {
        Long roomId = 1L;
        Instant reservationStartTime = Instant.parse("2024-08-24T12:00:00Z");
        Instant reservationEndTime = Instant.parse("2024-08-24T13:00:00Z");
        Room room = roomService.findRoomById(roomId);
        LocalDate date = reservationStartTime.atZone(ZoneOffset.UTC).toLocalDate();
        log.info(date.toString());

        // 룸과 날짜로 정책 찾기
        RoomOperationPolicySchedule schedule
                = scheduleRepository.findByRoomAndPolicyApplicationDate(room, date)
                .orElseThrow(
                        // 운영이 하지 않음 (운영 정책 없음)
                        () -> new RoomPolicyNotFoundException(room, date)
                );
//        roomService.isRoomAvailable(1L, startTime, endTime);
        RoomOperationPolicy roomOperationPolicy = schedule.getRoomOperationPolicy();
        Instant operationStartTime
                = DateTimeUtil.convertKstToUtc(
                date.atTime(roomOperationPolicy.getOperationStartTime())
        ).atZone(ZoneOffset.UTC).toInstant();

        Instant operationEndTime
                = DateTimeUtil.convertKstToUtc(
                date.atTime(roomOperationPolicy.getOperationEndTime())
        ).atZone(ZoneOffset.UTC).toInstant();

        log.info(operationStartTime.toString());
        log.info(operationEndTime.toString());

        boolean operationBefore = reservationStartTime.isBefore(operationStartTime);
        log.info("reservationstartTime < operationStartTime 인지 {}", operationBefore);

        boolean operationAfter = reservationEndTime.isAfter(operationEndTime);
        log.info("operationEndTime < reservationEndTime 인지 {}", operationAfter);

        if (operationAfter || operationBefore) {
            throw new OperationClosedException(
                    room,
                    operationStartTime, operationEndTime,
                    reservationStartTime, reservationEndTime);
        }
    }
}
