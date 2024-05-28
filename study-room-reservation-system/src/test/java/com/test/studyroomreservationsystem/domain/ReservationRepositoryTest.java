//package com.test.studyroomreservationsystem.domain;
//
//import com.test.studyroomreservationsystem.domain.entity.Reservation;
//import com.test.studyroomreservationsystem.domain.entity.Room;
//import com.test.studyroomreservationsystem.domain.entity.User;
//import com.test.studyroomreservationsystem.domain.repository.ReservationRepository;
//import com.test.studyroomreservationsystem.domain.repository.RoomRepository;
//import com.test.studyroomreservationsystem.domain.repository.UserRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//
//import java.time.LocalDateTime;
//import java.time.ZonedDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//
//@Slf4j
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class ReservationRepositoryTest {
//
//    @Autowired
//    ReservationRepository reservationRepository;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    RoomRepository roomRepository;
//
//    private Room room;
//    private User user;
//    void beforeRemove() {
//        reservationRepository.deleteAll();
//        roomRepository.deleteAll();
//        userRepository.deleteAll();
//    }
//
//    // test 에 사용될 인스턴스 세팅
//    void setUp() {
//        Room room = new Room();
//        room.setRoomName("306-1");
//        this.room = roomRepository.save(room);
//
//        User user = new User();
//        user.setUsername("hwangbbang");
//        user.setPassword("1234");
//        user.setSerial("202103769");
//        user.setIsAdmin(true);
//        this.user = userRepository.save(user);
//
//    }
//
//    private Reservation createReservation(ZonedDateTime reservationStartTime, ZonedDateTime reservationEndTime) {
//        Reservation reservation = new Reservation();
//        reservation.setUser(user);
//        reservation.setRoom(room);
//        reservation.setReservationStartTime(reservationStartTime);
//        reservation.setReservationEndTime(reservationEndTime);
//        reservation.setState(ReservationState.RESERVED);
//        return reservation;
//    }
//    @BeforeEach
//    void initWork() {
//        beforeRemove();
//        setUp();
//    }
//
//    @Test
//    void save() {
//        // given
//        ZonedDateTime reservationStartTIme = ZonedDateTime.now();
//        ZonedDateTime reservationEndTIme = reservationStartTIme.plusHours(1).plusMinutes(30);
//
//        Reservation reservation = createReservation(reservationStartTIme, reservationEndTIme);
//
//        // when
//        Reservation savedReservation = reservationRepository.save(reservation);
//
//        // then
//        assertThat(savedReservation).isNotNull();
//        assertThat(savedReservation.getRoom()).isEqualTo(reservation.getRoom());
//        assertThat(savedReservation.getUser()).isEqualTo(reservation.getUser());
//        assertThat(savedReservation.getReservationStartTime()).isEqualTo(reservation.getReservationStartTime());
//        assertThat(savedReservation.getReservationEndTime()).isEqualTo(reservation.getReservationEndTime());
//        assertThat(savedReservation.getState()).isEqualTo(reservation.getState());
//
//    }
//    @Test
//    void testFindAllByUser() {
//        // given
//        ZonedDateTime reservationStartTime = ZonedDateTime.now();
//        ZonedDateTime reservationEndTime = reservationStartTime.plusHours(1).plusMinutes(30);
//
//        Reservation reservation1 = createReservation(reservationStartTime, reservationEndTime);
//
//        reservationRepository.save(reservation1);
//
//
//        reservationStartTime = reservationEndTime.plusHours(2);
//        reservationEndTime = reservationStartTime.plusHours(3).plusMinutes(30);
//
//        Reservation reservation2 = createReservation(reservationStartTime, reservationEndTime);
//
//        reservationRepository.save(reservation2);
//        // when
//        Optional<List<Reservation>> reservationsByUsers = reservationRepository.findAllByUser(user);
//
//        // then
//        Assertions.assertTrue(reservationsByUsers.isPresent());
//        assertThat(reservationsByUsers.get()).hasSize(2);
//        assertThat(reservationsByUsers.get())
//                .extracting(Reservation::getUser)
//                .containsOnly(user);
//    }
//
//
//    @Test
//    void okTestFindOverlappingReservations() {
//        // given
//        ZonedDateTime defaultTime = ZonedDateTime.now();
//
//        ZonedDateTime startTime1 =  defaultTime;
//        ZonedDateTime endTime1 = defaultTime.plusHours(1);
//        Reservation overlappingReservation1 = createReservation(startTime1, endTime1);
//        reservationRepository.save(overlappingReservation1);
//
//        ZonedDateTime startTime2 = defaultTime.plusHours(1);
//        ZonedDateTime endTime2 = defaultTime.plusHours(1).plusMinutes(30);
//        Reservation overlappingReservation2 = createReservation(startTime2, endTime2);
//        reservationRepository.save(overlappingReservation2);
//
//        // overlappingReservation1 : 0 시간 ~ 1시간
//        // overlappingReservation2 : 1 시간 ~ 1시간 30 분
//        // assertionReservation  : 0 시간 ~ 2시간
//        ZonedDateTime assertionStartTime = defaultTime;
//        ZonedDateTime assertionEndTime = defaultTime.plusHours(2);
//
//        // when
//        List<Reservation> foundReservations
//                = reservationRepository.findOverlappingReservations(room.getRoomId(), assertionStartTime, assertionEndTime);
//
//        // then
//
//        assertThat(foundReservations).hasSize(2)
//                .extracting("reservationId")
//                .containsExactlyInAnyOrder(
//                        overlappingReservation1.getReservationId(),
//                        overlappingReservation2.getReservationId()
//                );
//
//        assertThat(foundReservations)
//                .extracting("state")
//                .containsOnly(ReservationState.RESERVED);
//    }
//    @Test
//    void notOkTestFindOverlappingReservations() {
//        // given
//
//        // nonOverlappingReservation1 : 1 시간 ~ 1 시간 30
//        ZonedDateTime defaultTime = ZonedDateTime.now();
//
//        ZonedDateTime startTime1 = defaultTime.plusHours(1);
//        ZonedDateTime endTime1 = defaultTime.plusHours(1).plusMinutes(30);
//        Reservation nonOverlappingReservation1 = createReservation(startTime1, endTime1);
//        reservationRepository.save(nonOverlappingReservation1);
//
//        // nonOverlappingReservation2 : 0 시간 ~ 0 시간 30
//        ZonedDateTime startTime2 = defaultTime.plusHours(0);
//        ZonedDateTime endTime2 = defaultTime.plusMinutes(30);
//        Reservation nonOverlappingReservation2 = createReservation(startTime2, endTime2);
//        reservationRepository.save(nonOverlappingReservation2);
//
//        // assertionReservation  : 0 시간 30 ~ 1 시간
//        ZonedDateTime assertionStartTime = defaultTime.plusMinutes(30);
//        ZonedDateTime assertionEndTime = defaultTime.plusHours(1);
//
//        // when
//        List<Reservation> foundReservations
//                = reservationRepository.findOverlappingReservations(room.getRoomId(), assertionStartTime, assertionEndTime);
//
//        // then
//        assertThat(foundReservations).hasSize(0);
//
//    }
//}