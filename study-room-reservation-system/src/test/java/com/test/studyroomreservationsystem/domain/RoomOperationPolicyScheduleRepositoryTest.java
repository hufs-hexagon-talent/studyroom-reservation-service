package com.test.studyroomreservationsystem.domain;

import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicy;
import com.test.studyroomreservationsystem.domain.entity.RoomOperationPolicySchedule;
import com.test.studyroomreservationsystem.domain.repository.RoomOperationPolicyRepository;
import com.test.studyroomreservationsystem.domain.repository.RoomOperationPolicyScheduleRepository;
import com.test.studyroomreservationsystem.domain.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoomOperationPolicyScheduleRepositoryTest {

    @Autowired
    RoomOperationPolicyScheduleRepository scheduleRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    RoomOperationPolicyRepository policyRepository;

    private Room room1;
    private Room room2;
    private RoomOperationPolicy policy1;
    private RoomOperationPolicy policy2;

    void removeAll() {
        scheduleRepository.deleteAll();
        roomRepository.deleteAll();
        policyRepository.deleteAll();
    }
    void setUp() {
        Room room1 = new Room();
        room1.setRoomName("306-1");
        this.room1 = roomRepository.save(room1);

        Room room2 = new Room();
        room2.setRoomName("306-2");
        this.room2 = roomRepository.save(room2);

        LocalTime defaultTime = LocalTime.now();
        LocalTime operationStartTime1 = defaultTime.plusHours(0);
        LocalTime operationEndTime1 = defaultTime.plusHours(8);

        LocalTime operationStartTime2 = defaultTime.plusHours(0);
        LocalTime operationEndTime2 = defaultTime.plusHours(12);

        RoomOperationPolicy policy1 = new RoomOperationPolicy();
        policy1.setOperationStartTime(operationStartTime1);
        policy1.setOperationEndTime(operationEndTime1);
        policy1.setEachMaxMinute(180);
        this.policy1 = policyRepository.save(policy1);

        RoomOperationPolicy policy2 = new RoomOperationPolicy();
        policy2.setOperationStartTime(operationStartTime2);
        policy2.setOperationEndTime(operationEndTime2);
        policy2.setEachMaxMinute(240);
        this.policy2 = policyRepository.save(policy2);
    }
    private RoomOperationPolicySchedule createSchedule(LocalDate defaultDate, Room room, RoomOperationPolicy policy) {
        RoomOperationPolicySchedule schedule = new RoomOperationPolicySchedule();
        schedule.setRoom(room);
        schedule.setRoomOperationPolicy(policy);
        schedule.setPolicyApplicationDate(defaultDate);
        return schedule;
    }

    private void setOneWeekPolicy(LocalDate defaultDate) {
        for (int i = 0; i < 7; i++) {
            if (i == 5 || i == 6) {
                scheduleRepository.save(createSchedule(defaultDate.plusDays(i), room1, policy2));
            } else {
                scheduleRepository.save(createSchedule(defaultDate.plusDays(i), room1, policy1));
            }
        }
        log.info("Total schedules saved: {}",scheduleRepository.count());
    }
    @BeforeEach
    void initWork() {
        removeAll();
        setUp();
    }
    @Test
    void save() {
        //given
        LocalDate defaultDate = LocalDate.now();
        RoomOperationPolicySchedule schedule = createSchedule(defaultDate,room1,policy1);

        //when
        RoomOperationPolicySchedule savedSchedule = scheduleRepository.save(schedule);

        //then
        assertThat(savedSchedule).isNotNull();

        assertThat(savedSchedule.getRoomOperationPolicyScheduleId())
                .isEqualTo(schedule.getRoomOperationPolicyScheduleId());

        assertThat(savedSchedule.getRoom()).isEqualTo(schedule.getRoom());

        assertThat(savedSchedule.getRoomOperationPolicy())
                .isEqualTo(schedule.getRoomOperationPolicy());
        
        assertThat(savedSchedule.getPolicyApplicationDate()).
                isEqualTo(schedule.getPolicyApplicationDate());

    }
    @Test
    void testFindByRoomAndPolicyApplicationDate() {
        //given
        LocalDate defaultDate = LocalDate.now();
        LocalDate nextDate = defaultDate.plusDays(1);
        RoomOperationPolicySchedule schedule1 = createSchedule(defaultDate,room1,policy1);
        scheduleRepository.save(schedule1);

        RoomOperationPolicySchedule schedule2 = createSchedule(defaultDate,room2,policy1);
        scheduleRepository.save(schedule2);

        RoomOperationPolicySchedule schedule3 = createSchedule(nextDate,room1,policy2);
        scheduleRepository.save(schedule3);

        //when
        Optional<RoomOperationPolicySchedule> foundSchedule = scheduleRepository
                .findByRoomAndPolicyApplicationDate(
                        schedule1.getRoom(),
                        schedule1.getPolicyApplicationDate()
                );

        //then
        assertTrue(foundSchedule.isPresent());

        assertThat(foundSchedule.get().getRoomOperationPolicyScheduleId())
                .isEqualTo(schedule1.getRoomOperationPolicyScheduleId());

        assertThat(foundSchedule.get().getRoom())
                .isEqualTo(schedule1.getRoom());

        assertThat(foundSchedule.get().getRoomOperationPolicy())
                .isEqualTo(schedule1.getRoomOperationPolicy());

        assertThat(foundSchedule.get().getPolicyApplicationDate())
                .isEqualTo(schedule1.getPolicyApplicationDate());

    }
    @Test
    void testUniqueRoomIdAndPolicyApplicationDate() {
        // given
        LocalDate defaultDate = LocalDate.now();
        RoomOperationPolicySchedule schedule1 = createSchedule(defaultDate, room1, policy1);
        scheduleRepository.save(schedule1);
        //when
        RoomOperationPolicySchedule schedule2 = createSchedule(defaultDate, room1, policy2);
        RoomOperationPolicySchedule schedule3 = createSchedule(defaultDate, room2, policy1);
        RoomOperationPolicySchedule schedule4 = createSchedule(defaultDate.plusDays(1), room1, policy1);
        
        //then
        // 같은 날 같은 방 에 유니크 제약 조건 위배
        assertThrows(DataIntegrityViolationException.class,
                () -> scheduleRepository.saveAndFlush(schedule2));

        // 같은 날 다른 방 에 대해서는 이상 없음
        scheduleRepository.save(schedule3);

        // 다른 날 같은 방 에 대해서는 이상 없음
        scheduleRepository.save(schedule4);
    }
    @Test
    void testFindByRoomAndPolicyApplicationDateBetween() {
        //given
        LocalDate defaultDate = LocalDate.now();
        RoomOperationPolicySchedule schedule1 = createSchedule(defaultDate,room1,policy1);
        RoomOperationPolicySchedule schedule2 = createSchedule(defaultDate.plusDays(1),room1,policy2);
        RoomOperationPolicySchedule schedule3 = createSchedule(defaultDate.plusDays(2),room1,policy1);
        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);
        scheduleRepository.save(schedule3);
        
        //when
        List<RoomOperationPolicySchedule> schedules 
                = scheduleRepository.findByRoomAndPolicyApplicationDateBetween(room1, defaultDate, defaultDate.plusDays(2));
        
        //then
        // 검증 코드 구현, schedule 리스트가 구해질것이야 .
        assertThat(schedules).hasSize(3);
        assertThat(schedules).extracting(RoomOperationPolicySchedule::getRoom)
                .containsOnly(room1);

        // Date 검증
        assertThat(schedules).extracting(RoomOperationPolicySchedule::getPolicyApplicationDate)
                .containsExactly(defaultDate, defaultDate.plusDays(1), defaultDate.plusDays(2));

        // policy 검증
        assertThat(schedules).extracting(RoomOperationPolicySchedule::getRoomOperationPolicy)
                .containsExactly(policy1,policy2,policy1);
    }
    @Test
    void testFindByPolicyApplicationDate() {
        // given
        LocalDate defaultDate = LocalDate.now();
        RoomOperationPolicySchedule schedule1 = createSchedule(defaultDate,room1,policy1);
        RoomOperationPolicySchedule schedule2 = createSchedule(defaultDate,room2,policy2);

        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);

        // when
        List<RoomOperationPolicySchedule> schedules
                = scheduleRepository.findByPolicyApplicationDate(defaultDate);

        // then
        assertThat(schedules).hasSize(2);

        assertThat(schedules).extracting(RoomOperationPolicySchedule::getPolicyApplicationDate)
                .containsOnly(defaultDate);

        assertThat(schedules).extracting(RoomOperationPolicySchedule::getRoom)
                .containsExactly(schedule1.getRoom(), schedule2.getRoom());

        assertThat(schedules).extracting(RoomOperationPolicySchedule::getRoomOperationPolicy)
                .containsExactly(schedule1.getRoomOperationPolicy(), schedule2.getRoomOperationPolicy());

    }

    @Test
    void testFindByPolicyApplicationDateBetween() {
        // given
        LocalDate defaultDate = LocalDate.now();
        setOneWeekPolicy(defaultDate);

        //when
        List<RoomOperationPolicySchedule> schedules
                = scheduleRepository.findByPolicyApplicationDateBetween(defaultDate, defaultDate.plusDays(6));
        // then
        assertThat(schedules).hasSize(7);
        assertThat(schedules).extracting(RoomOperationPolicySchedule::getRoom)
                .containsOnly(room1);

        assertThat(schedules).extracting(RoomOperationPolicySchedule::getRoomOperationPolicy)
                .containsExactly(policy1,policy1,policy1,policy1,policy1,policy2,policy2);

        assertThat(schedules).extracting(RoomOperationPolicySchedule::getPolicyApplicationDate)
                .containsExactly(
                        defaultDate.plusDays(0),
                        defaultDate.plusDays(1),
                        defaultDate.plusDays(2),
                        defaultDate.plusDays(3),
                        defaultDate.plusDays(4),
                        defaultDate.plusDays(5),
                        defaultDate.plusDays(6)
                );
    }

}
