package hufs.computer.studyroom.common.error.exception.notfound;

import hufs.computer.studyroom.domain.room.entity.Room;

import java.time.LocalDate;

public class ScheduleNotFoundException extends RuntimeException implements NotFoundException{

    public ScheduleNotFoundException(Room room, LocalDate date) {
        super(date + ": " + room.getRoomName() + "를 찾을 수 없습니다.");
    }
    public ScheduleNotFoundException(Long scheduleId) {
        super("id가 " + scheduleId + "인 Schedule 을 찾을 수 없습니다.");
    }
}
