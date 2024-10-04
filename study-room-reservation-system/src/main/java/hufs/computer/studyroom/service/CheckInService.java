package hufs.computer.studyroom.service;

import hufs.computer.studyroom.dto.CheckInReservationDto;

import java.util.List;

public interface CheckInService {
     List<CheckInReservationDto> verifyCheckIn(String verificationCode, Long roomId);
}
