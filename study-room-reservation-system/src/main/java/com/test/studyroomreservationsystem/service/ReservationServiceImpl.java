package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.repository.ReservationRepository;
import com.test.studyroomreservationsystem.dto.ReservationCreateDto;
import com.test.studyroomreservationsystem.dto.ReservationUpdateDto;
import com.test.studyroomreservationsystem.service.exception.ReservationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private ReservationRepository reservationRepository;
    private final RoomService roomService;
    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, RoomService roomService) {
        this.reservationRepository = reservationRepository;
        this.roomService = roomService;
    }

    @Override
    public Reservation createReservation(ReservationCreateDto reservationDto) {
        // 예약 가능 여부 확인 로직
        // 룸이 운영시간인지 ?
        if(roomService.isRoomAvailable(reservationDto.getRoomId(), reservationDto)){
            ;;
        }
        // 다른 예약이 없는지 ?
        Reservation reservation = new Reservation();
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation findReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(()->new ReservationNotFoundException("Reservation not found with id: " + reservationId));
    }

    @Override
    public List<Reservation> findAllReservation() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation updateReservation(Long reservationId, ReservationUpdateDto updateParam) {
        return null;
    }

    @Override
    public void deleteReservationById(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}
