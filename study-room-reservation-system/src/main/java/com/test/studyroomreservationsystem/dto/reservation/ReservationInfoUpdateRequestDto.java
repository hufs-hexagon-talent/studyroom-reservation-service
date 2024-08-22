package com.test.studyroomreservationsystem.dto.reservation;

import com.test.studyroomreservationsystem.domain.entity.Reservation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.test.studyroomreservationsystem.domain.entity.Reservation.*;

@Getter
@NoArgsConstructor
public class ReservationInfoUpdateRequestDto {
    private ReservationState state;
    @Builder
    public ReservationInfoUpdateRequestDto(ReservationState state) {
        this.state = state;
    }
    // from :  Entity -> Dto
    public void toEntity(Reservation reservation) {
        if (this.state != null) {
            reservation.setState(this.state);
        }
        }
}
