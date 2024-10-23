package hufs.computer.studyroom.domain.department.service;

import hufs.computer.studyroom.common.error.code.DepartmentErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.domain.department.dto.response.DepartmentInfoResponse;
import hufs.computer.studyroom.domain.department.entity.Department;
import hufs.computer.studyroom.domain.department.mapper.DepartmentMapper;
import hufs.computer.studyroom.domain.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DepartmentQueryService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    /* Query */
    public DepartmentInfoResponse findDepartmentById(Long departmentId) {
        Department department = getDepartmentById(departmentId);
        return departmentMapper.toInfoResponse(department);
    }

    /* Query */
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new CustomException(DepartmentErrorCode.DEPARTMENT_NOT_FOUND));
    }
    /* Query */
    public boolean existByDepartmentId(Long departmentId) {
        return departmentRepository.existsById(departmentId);
    }
}
