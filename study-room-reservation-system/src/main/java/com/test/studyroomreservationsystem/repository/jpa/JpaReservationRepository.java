package com.test.studyroomreservationsystem.repository.jpa;

import com.test.studyroomreservationsystem.domain.Reservation;
import com.test.studyroomreservationsystem.repository.ReservationRepository;
import com.test.studyroomreservationsystem.repository.ReservationUpdateDto;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@Transactional
public class JpaReservationRepository implements ReservationRepository {
    private final EntityManager em;

    public JpaReservationRepository(EntityManager em) {
        this.em = em;
    }

    private static long sequence = 0L; //static

    @Override
    public Reservation save(Reservation reservation) {
//        put(reservation.getReservationId(),reservation)
        reservation.setReservationId(++sequence);
        em.persist(reservation);
        return reservation;
    }

    @Override
    public void update(Long reservationId, ReservationUpdateDto updateParam) {
        Reservation findedReservation = em.find(Reservation.class, reservationId);
        findedReservation.setState(updateParam.getState());
        findedReservation.setPartitionId(updateParam.getPartitionId());
        findedReservation.setRoomId(updateParam.getRoomId());
    }

    @Override
    public Optional<Reservation> findByReservationId(Long reservationId) {
        Reservation reservation = em.find(Reservation.class, reservationId);
        return Optional.ofNullable(reservation);

    }


    @Override
    public void deleteByReservationId(Long reservationId) {
//        Reservation findedReservation = findByReservationId(reservationId).orElseThrow();
        Reservation findedReservation = em.find(Reservation.class, reservationId);
        findedReservation.setState(null);
        findedReservation.setRoomId(null);
        findedReservation.setPartitionId(null);
        findedReservation.setReservationId(null);
    }
//    @Override
//    public List<Reservation> findAll() {
//        return null;
//    }

}
