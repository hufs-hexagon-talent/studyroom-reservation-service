package hufs.computer.studyroom.domain.schedule.repository;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.schedule.entity.RoomOperationPolicySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomOperationPolicyScheduleRepository extends JpaRepository<RoomOperationPolicySchedule, Long> {
    // JpaRepository 에서 인터페이스 CrudRepository 가 기본 CRUD 기능을 제공

    // {특정 날짜에 해당, 특정 방} 의 운영 정책 스케쥴 을 찾는 메소드
    Optional<RoomOperationPolicySchedule> findByRoomAndPolicyApplicationDate(Room room, LocalDate date);
    boolean existsByRoomRoomIdAndPolicyApplicationDate(Long roomId, LocalDate date);
    boolean existsByRoomAndPolicyApplicationDate(Room room, LocalDate date);


    // {특정 날짜에 해당 하는 방들} 의 운영 정책 스케쥴 을 찾는 메소드
    List<RoomOperationPolicySchedule> findByPolicyApplicationDate(LocalDate date);


    // 메인 API → 특정 부서에서 금일 기점으로 미래까지, 어떤방들을 이용할 수 있는지? 단,방들을 날짜로 묶어(Group by) 응답
    @Query(
            "SELECT DISTINCT this.policyApplicationDate " +
            "FROM RoomOperationPolicySchedule this " +
            "WHERE this.policyApplicationDate >= :startDate AND this.room.department.departmentId = :departmentId " +
            "ORDER BY this.policyApplicationDate"
    )
    List<LocalDate> findAvailableRoomsGroupedByDate(@Param("startDate") LocalDate startDate,@Param("departmentId") Long departmentId);


    /**
     * 주어진 룸 ID와 특정 날짜에 대해, 다른 스케줄 ID를 가진 예약 정책이 존재하는지 확인하는 메서드.
     *
     * @param roomId           룸의 고유 ID
     * @param date             예약 정책이 적용될 날짜
     * @param scheduleId       제외할 예약 정책의 ID (같은 날짜와 룸에 다른 예약 정책이 있는지 확인할 때 사용)
     * @return                 주어진 룸 ID와 날짜에 해당하는 다른 예약 정책이 존재하면 true, 그렇지 않으면 false
     */
    boolean existsByRoomRoomIdAndPolicyApplicationDateAndRoomOperationPolicyScheduleIdNot(Long roomId, LocalDate date, Long scheduleId);

}
