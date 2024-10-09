package hufs.computer.studyroom.domain.department.service;

import hufs.computer.studyroom.common.error.code.DepartmentErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.domain.department.dto.request.CreateDepartmentRequest;
import hufs.computer.studyroom.domain.department.dto.request.ModifyDepartmentRequest;
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
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    /* Command */
    public DepartmentInfoResponse createDepartment(CreateDepartmentRequest request) {
        Department department = departmentMapper.toDepartment(request);
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toInfoResponse(savedDepartment);
    }
    /* Command */
    public DepartmentInfoResponse updateDepartment(Long departmentId, ModifyDepartmentRequest request) {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new CustomException(DepartmentErrorCode.DEPARTMENT_NOT_FOUND));
        departmentMapper.updateDepartmentFromRequest(request, department);
        departmentRepository.save(department);
        return departmentMapper.toInfoResponse(department);
    }
    /* Command */
    public void deleteDepartment(Long departmentId) {
        findDepartmentById(departmentId);
        departmentRepository.deleteById(departmentId);
    }

    /* Query */
    public DepartmentInfoResponse findDepartmentById(Long departmentId) {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new CustomException(DepartmentErrorCode.DEPARTMENT_NOT_FOUND));
        return departmentMapper.toInfoResponse(department);
    }

}
