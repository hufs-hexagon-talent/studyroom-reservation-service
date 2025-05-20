package hufs.computer.studyroom.domain.reservation.repository;

import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.reservation.dto.request.ReservationSearchCondition;
import hufs.computer.studyroom.domain.reservation.entity.Reservation;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.user.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.List;

import static hufs.computer.studyroom.domain.reservation.entity.Reservation.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationSpecification {
    public static Specification<Reservation> search(ReservationSearchCondition cond){
        return Specification.where(userId(cond.userId())
                .and(username(cond.username())))
                .and(serial(cond.serial()))
                .and(name(cond.name()))
                .and(roomIds(cond.roomIds()))
                .and(roomPartitionIds(cond.roomPartitionIds()))
                .and(dateRange(cond.startDateTime(),cond.endDateTime()))
                .and(state(cond.states()));
    }

    /* ====== 필드별 ─ 내부 빌더 ====== */


    private static Specification<Reservation> likeOnUser(String field, String value) {
        return (value == null || value.isBlank()) ? null
                : (root, q, cb) -> {
            Join<Reservation, User> user = root.join("user");
            return cb.like(cb.lower(user.get(field)), "%" + value.toLowerCase() + "%");
        };
    }

    private static Specification<Reservation> userId(String value) {return likeOnUser("userId", value);}
    private static Specification<Reservation> username(String value) {return likeOnUser("username", value);}
    private static Specification<Reservation> serial(String value) {return likeOnUser("serial", value);}
    private static Specification<Reservation> name(String value) {return likeOnUser("name", value);}



    private static Specification<Reservation> roomIds(List<Long> ids) {
        return (ids == null || ids.isEmpty()) ? null
                : (root, q, cb) -> {
            Join<Reservation, RoomPartition> part = root.join("roomPartition");
                    Join<RoomPartition, Room> room = part.join("room");
            return room.get("roomId").in(ids);
        };
    }

    private static Specification<Reservation> roomPartitionIds(List<Long> ids) {
        return (ids == null || ids.isEmpty()) ? null
                : (root, q, cb) ->
                root.get("roomPartition").get("roomPartitionId").in(ids);
    }


    private static Specification<Reservation> dateRange(Instant start, Instant end) {
        if (start == null && end == null) return null;

        return (root, query, cb) -> {
            Path<Instant> resStart = root.get("reservationStartTime");
            Path<Instant> resEnd   = root.get("reservationEndTime");

            // ① 조회 구간이 모두 들어온 경우
            if (start != null && end != null) {
                return cb.and(
                        cb.greaterThan(resEnd, start),   // resEnd  > filterStart
                        cb.lessThan(resStart, end)       // resStart < filterEnd
                );
            }

            // start 만 있으면 “start 이후에 끝나는 예약”
            if (start != null) {
                return cb.greaterThan(resEnd, start);
            }

            // end 만 있으면 “end 이전에 시작한 예약”
            return cb.lessThan(resStart, end);
        };
    }

    private static Specification<Reservation> state(List<ReservationState> states) {
        return (states == null || states.isEmpty()) ? null
                : (root, q, cb) -> root.get("state").in(states);
    }
}
