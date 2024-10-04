package hufs.computer.studyroom.domain.reservation.service;

import hufs.computer.studyroom.domain.auth.dto.CheckInReservationDto;

import java.util.List;

public interface CheckInService {
     List<CheckInReservationDto> verifyCheckIn(String verificationCode, Long roomId);
}
