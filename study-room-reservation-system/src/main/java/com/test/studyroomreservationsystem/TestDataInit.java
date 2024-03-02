package com.test.studyroomreservationsystem;

import com.test.studyroomreservationsystem.domain.Reservation;
import com.test.studyroomreservationsystem.domain.State;
import com.test.studyroomreservationsystem.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
public class TestDataInit {
    private final ReservationRepository reservationRepository;

    /*테스트용 초기 데이터*/

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.info("TEST DATE INIT");

        reservationRepository.save(new Reservation(306,1,1L, State.RESERVATION));
         reservationRepository.save(new Reservation(428,2,2L, State.VISITED));
    }

}
