package com.test.studyroomreservationsystem.repository;

import com.test.studyroomreservationsystem.domain.State;
import lombok.Data;

@Data
public class ReservationUpdateDto {
    private Enum<State> state;

    public ReservationUpdateDto() {
    }
    public ReservationUpdateDto(Enum<State> state) {
        this.state = state;
    }


}
