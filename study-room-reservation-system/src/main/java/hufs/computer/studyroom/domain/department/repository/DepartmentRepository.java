package hufs.computer.studyroom.domain.department.repository;

import hufs.computer.studyroom.domain.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
