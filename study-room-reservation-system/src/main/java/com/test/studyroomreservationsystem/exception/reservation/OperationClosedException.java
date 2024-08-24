package com.test.studyroomreservationsystem.exception.reservation;

import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.service.DateTimeUtil;

import java.time.Instant;

public class OperationClosedException extends RuntimeException implements ReservationNotPossibleException {

    // 운영 시간 사용안할 수 있음
    public OperationClosedException(Room room,
                                    Instant operationStartTime,
                                    Instant operationEndTime,
                                    Instant reservationStartTime,
                                    Instant reservationEndTime) {
        super(buildErrorMessage(room, operationStartTime, operationEndTime, reservationStartTime, reservationEndTime));
    }

    private static String buildErrorMessage(Room room,
                                            Instant operationStartTime,
                                            Instant operationEndTime,
                                            Instant reservationStartTime,
                                            Instant reservationEndTime) {
        String operationTime = formatTimeRange(operationStartTime, operationEndTime);
        String reservationTime = formatTimeRange(reservationStartTime, reservationEndTime);

        return String.format("현재 %s은 운영시간은 %s 입니다. 예약하신 시간은 %s 입니다.",
                room.getRoomName(), operationTime, reservationTime);
    }

    private static String formatTimeRange(Instant startTime, Instant endTime) {
        return DateTimeUtil.instantToLocalDateTime(startTime).format(DateTimeUtil.FORMATTER) +
                " ~ " +
                DateTimeUtil.instantToLocalDateTime(endTime).format(DateTimeUtil.FORMATTER);
    }
}
