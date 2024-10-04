package hufs.computer.studyroom.domain.schedule.repository;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomOperationPolicyScheduleRepository extends JpaRepository<RoomOperationPolicySchedule, Long> {
    // JpaRepository 에서 인터페이스 CrudRepository 가 기본 CRUD 기능을 제공

    // {특정 날짜에 해당, 특정 방} 의 운영 정책 스케쥴 을 찾는 메소드
    Optional<RoomOperationPolicySchedule>
    findByRoomAndPolicyApplicationDate(Room room, LocalDate date);

    // {여러 날짜에 해당, 특정 방} 의 운영 정책 스케쥴 을 찾는 메소드
    List<RoomOperationPolicySchedule>
    findByRoomAndPolicyApplicationDateBetween(Room room, LocalDate startDate, LocalDate endDate);

    // {특정 날짜에 해당 하는 방들} 의 운영 정책 스케쥴 을 찾는 메소드
    List<RoomOperationPolicySchedule>
    findByPolicyApplicationDate(LocalDate date);


    // {여러 날짜에 해당 하는 방들} 의 운영 정책 스케쥴 을 찾는 메소드
    List<RoomOperationPolicySchedule>
    findByPolicyApplicationDateBetween(LocalDate startDate, LocalDate endDate);

    // ( 레포지토리 계층)
    // 메인 API → 금일 기점으로 미래까지, 어떤방들을 이용할 수 있는지? 단,방들을 날짜로 묶어(Group by) 응답
    @Query(
            "SELECT DISTINCT this.policyApplicationDate " +
            "FROM RoomOperationPolicySchedule this " +
            "WHERE this.policyApplicationDate >= :startDate " +
            "ORDER BY this.policyApplicationDate"
    )
    List<LocalDate> findAvailableRoomsGroupedByDate(@Param("startDate") LocalDate startDate);


}
