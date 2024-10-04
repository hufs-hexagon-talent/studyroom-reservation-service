package hufs.computer.studyroom.domain.repository;

import hufs.computer.studyroom.domain.entity.RoomOperationPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomOperationPolicyRepository extends JpaRepository<RoomOperationPolicy,Long> {
    // JpaRepository 에서 인터페이스 CrudRepository 가 기본 CRUD 기능을 제공
}
