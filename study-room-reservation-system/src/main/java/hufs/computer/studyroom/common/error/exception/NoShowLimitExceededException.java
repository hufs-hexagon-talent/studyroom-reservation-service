package hufs.computer.studyroom.common.error.exception;

import hufs.computer.studyroom.common.error.exception.reservation.ReservationNotPossibleException;
import hufs.computer.studyroom.common.util.DateTimeUtil;

import java.time.Instant;

public class NoShowLimitExceededException extends RuntimeException implements ReservationNotPossibleException {
    public NoShowLimitExceededException(Instant startDate, Instant endDate)  {

        super("No Show 횟수를 초과 하여 "+ DateTimeUtil.instantToLocalDateTime(startDate).format(DateTimeUtil.FORMATTER)+" ~ "
                +        DateTimeUtil.instantToLocalDateTime(endDate).format(DateTimeUtil.FORMATTER)
                +" 동안 이용이 불가능 합니다.");

    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
