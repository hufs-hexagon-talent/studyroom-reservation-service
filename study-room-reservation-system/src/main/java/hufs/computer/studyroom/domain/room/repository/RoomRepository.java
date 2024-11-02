package hufs.computer.studyroom.domain.room.repository;

import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import hufs.computer.studyroom.domain.room.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room,Long> {
    // JpaRepository 에서 인터페이스 CrudRepository 가 기본 CRUD 기능을 제공

    List<Room> findAllByDepartmentDepartmentId(Long departmentId);
}
