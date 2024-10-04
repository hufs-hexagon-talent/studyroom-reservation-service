package hufs.computer.studyroom.domain.auth.dto;

import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
public class CheckInResponseDto {
    private Instant checkInTime;
    private List<CheckInReservationDto> checkInReservations;

    public CheckInResponseDto(List<CheckInReservationDto> checkInReservations) {
        this.checkInTime = Instant.now();
        this.checkInReservations = checkInReservations;
    }
}
