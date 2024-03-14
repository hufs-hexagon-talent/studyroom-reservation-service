package com.test.studyroomreservationsystem.domain;

import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.repository.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.assertj.core.api.Assertions.*;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReservationRepositoryTest {

    @Autowired
    ReservationRepository reservationRepository;


    @BeforeEach
    void beforeRemove(){
        reservationRepository.deleteAll();
    }


    @Test
    void save() {
        // given
        Reservation reservation =
                new Reservation(202103769L, 1, 1, 428, 10, 11, State.RESERVATION);
        // when
        Reservation savedReservation = reservationRepository.save(reservation);

        // then
        Reservation findReservation =
                reservationRepository.findById(reservation.getReservationId()).get();

        assertThat(findReservation).isEqualTo(savedReservation);
    }

//    @Test
//    void update() {
//        // given
//        Reservation reservation = new Reservation();
//        reservation.setUserId(202103769L);
//        reservation.setPartitionId(1);
//        reservation.setTimetableId(1);
//        reservation.setRoomId(428);
//        reservation.setTimetableIndexFrom(10);
//        reservation.setTimetableIndexTo(11);
//        reservation.setState(State.RESERVATION);
//
//        Reservation savedReservation = reservationRepository.save(reservation);
//
//        new ReservationUpdateDto()
//
//        // when
//
//
//        // then
//        Reservation findReservation = reservationRepository.findByReservationId(reservationId).get();
//
//        assertThat(findReservation.getPartitionId()).isEqualTo(updateDto.getPartitionId());
//        assertThat(findReservation.getState()).isEqualTo(updateDto.getState());
//        assertThat(findReservation.getRoomId()).isEqualTo(updateDto.getRoomId());
//    }
    @Test
    void delete(){
        // given
        Reservation reservation = new Reservation(202103769L, 1, 1, 428, 10, 11, State.RESERVATION);
        Reservation savedReservation = reservationRepository.save(reservation);
        assertThat(savedReservation.getReservationId()).isEqualTo(reservation.getReservationId());

        Long reservationId = savedReservation.getReservationId();
        System.out.println("reservationId : "+reservationId);
        // when
        reservationRepository.deleteById(reservationId);
        boolean exists = reservationRepository.existsById(reservationId);

        // then
        assertThat(exists).isFalse();

    }
    @Test
    void deleteAll(){
        // given
        Reservation reservation1 = new Reservation(202103769L, 1, 1, 428, 10, 11, State.RESERVATION);
        Reservation reservation2 = new Reservation(202112345L, 2, 4, 306, 11, 12, State.VISITED);
        Reservation reservation3 = new Reservation(209999999L, 3, 2, 428, 12, 13, State.NOSHOW);
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
        reservationRepository.save(reservation3);
        assertThat(reservationRepository.count()).isEqualTo(3);

        // when
        reservationRepository.deleteAll();

        // then
        assertThat(reservationRepository.count()).isEqualTo(0);

    }

}