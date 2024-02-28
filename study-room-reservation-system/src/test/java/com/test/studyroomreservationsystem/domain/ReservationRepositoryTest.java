package com.test.studyroomreservationsystem.domain;

import com.test.studyroomreservationsystem.repository.ReservationRepository;
import com.test.studyroomreservationsystem.repository.ReservationUpdateDto;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.*;


@Slf4j
@Transactional
@SpringBootTest
public class ReservationRepositoryTest {

    @Autowired
    ReservationRepository reservationRepository;


    @Test
    void save() {
        // given
        Reservation reservation =
                new Reservation(306L, 1L, 1L,State.RESERVATION);
        // when
        Reservation savedReservation = reservationRepository.save(reservation);

        // then
        Reservation findReservation =
                reservationRepository.findByReservationId(reservation.getReservationId()).get();

        assertThat(findReservation).isEqualTo(savedReservation);
    }

    @Test
    void update(){
        // given
        Reservation reservation =
            new Reservation(306L, 1L, 1L,State.RESERVATION);
        Reservation savedReservation = reservationRepository.save(reservation);
        Long reservationId = savedReservation.getReservationId();

        // when
        ReservationUpdateDto updateDto = new ReservationUpdateDto(428L, 2L, State.VISITED);
        reservationRepository.update(reservationId,updateDto);

        // then
        Reservation findReservation = reservationRepository.findByReservationId(reservationId).get();

        assertThat(findReservation.getPartitionId()).isEqualTo(updateDto.getPartitionId());
        assertThat(findReservation.getState()).isEqualTo(updateDto.getState());
        assertThat(findReservation.getRoomId()).isEqualTo(updateDto.getRoomId());
    }
    @Test
    void delete(){
        // given
        Reservation reservation =
                new Reservation(306L, 1L, 1L,State.RESERVATION);
        Reservation savedReservation = reservationRepository.save(reservation);
        assertThat(savedReservation.getReservationId()).isEqualTo(reservation.getReservationId());
        Long reservationId = savedReservation.getReservationId();

        // when
        reservationRepository.deleteByReservationId(reservationId);

        // then
        assertThat(savedReservation.getReservationId()).isNull();
        assertThat(savedReservation.getPartitionId()).isNull();
        assertThat(savedReservation.getRoomId()).isNull();
        assertThat(savedReservation.getState()).isNull();

    }
}
