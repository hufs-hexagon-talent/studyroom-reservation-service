package hufs.computer.studyroom.dto.reservation;

import hufs.computer.studyroom.domain.entity.Reservation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationInfoUpdateRequestDto {
    private Reservation.ReservationState state;
    @Builder
    public ReservationInfoUpdateRequestDto(Reservation.ReservationState state) {
        this.state = state;
    }
    // from :  Entity -> Dto
    public void toEntity(Reservation reservation) {
        if (this.state != null) {
            reservation.setState(this.state);
        }
        }
}
