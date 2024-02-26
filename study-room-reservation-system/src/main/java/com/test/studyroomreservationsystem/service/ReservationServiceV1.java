package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.Reservation;
import com.test.studyroomreservationsystem.repository.ReservationRepository;
import com.test.studyroomreservationsystem.repository.ReservationUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationServiceV1 implements ReservationService{
    private final ReservationRepository reservationRepository;

    @Override
    public Reservation save(Reservation reservation) {
        return reservationRepository.save((reservation));
    }

    @Override
    public void update(Long reservationId, ReservationUpdateDto updateParam) {
        reservationRepository.update(reservationId, updateParam);
    }

    @Override
    public Optional<Reservation> findByReservationId(Long reservationId) {
        return reservationRepository.findByReservationId(reservationId);
    }

    @Override
    public void deleteByReservationId(Long reservationId) {
        reservationRepository.deleteByReservationId(reservationId);
    }
}
