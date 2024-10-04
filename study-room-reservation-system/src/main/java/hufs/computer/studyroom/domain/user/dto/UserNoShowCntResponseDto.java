package hufs.computer.studyroom.domain.user.dto;

import hufs.computer.studyroom.domain.reservation.dto.ReservationInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserNoShowCntResponseDto {
    private int noShowCount;
    private List<ReservationInfoResponseDto> reservationList;
}
