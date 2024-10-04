package hufs.computer.studyroom.domain.repository;

import hufs.computer.studyroom.domain.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room,Long> {
    // JpaRepository 에서 인터페이스 CrudRepository 가 기본 CRUD 기능을 제공
    Optional<Room> findByRoomName(String roomName);
}
