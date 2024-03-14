package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.ReservationState;
import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.repository.ReservationRepository;
import com.test.studyroomreservationsystem.dto.ReservationDto;
import com.test.studyroomreservationsystem.service.exception.ReservationNotFoundException;
import com.test.studyroomreservationsystem.service.exception.ReservationNotPossibleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final RoomService roomService;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, UserService userService, RoomService roomService) {
        this.reservationRepository = reservationRepository;
        this.userService = userService;
        this.roomService = roomService;
    }
    
    @Override
    public Reservation createReservation(ReservationDto reservationDto) {
        // 예약 가능 여부 확인 로직
        // 룸이 운영시간인지 ?  다른 예약이 없는지 ?
        boolean isAvailable = isOperatingAndNotOverlapping(reservationDto);
        if (!isAvailable) {
            // 에약 불가
            throw new ReservationNotPossibleException("The room is not available.");
        }
        Reservation reservation = new Reservation();
        reservationDtoToObj(reservationDto, reservation);
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
    public Reservation updateReservation(Long reservationId, ReservationDto reservationDto) {
        boolean isAvailable = isOperatingAndNotOverlapping(reservationDto);
        if (!isAvailable) {
            // 에약 변경 불가
            throw new ReservationNotPossibleException("The room is not available.");
        }
        //변경 가능
        Reservation reservation = new Reservation();
        reservationDtoToObj(reservationDto,reservation);
        return reservationRepository.save(reservation);
    }

    @Override
    public void deleteReservationById(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }
    private boolean isOperatingAndNotOverlapping(ReservationDto reservationDto) {
        return roomService.isRoomAvailable(reservationDto.getRoomId(), reservationDto)
                &&
                reservationRepository.findOverlappingReservations(
                        reservationDto.getRoomId(),
                        reservationDto.getStartDateTime(),
                        reservationDto.getEndDateTime()
                ).isEmpty();
    }


    // 이 메서드 수정해야댐 Reservation dto 가 어떤 정보를 변경할지 아직 정해지지않았음.
    private void reservationDtoToObj(ReservationDto reservationDto, Reservation reservation) {
//        reservation.setUser(userService.findUserById(reservationDto.getUserId()));
//        reservation.setRoom(roomService.findRoomById(reservationDto.getRoomId()));
        reservation.setState(ReservationState.RESERVATION);
        reservation.setEndDateTime(reservationDto.getEndDateTime());
        reservation.setState(reservationDto.getState());
    }

}
