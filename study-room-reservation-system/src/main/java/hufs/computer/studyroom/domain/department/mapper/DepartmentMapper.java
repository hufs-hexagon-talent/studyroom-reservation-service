package hufs.computer.studyroom.domain.department.mapper;

import hufs.computer.studyroom.domain.department.dto.request.CreateDepartmentRequest;
import hufs.computer.studyroom.domain.department.dto.request.ModifyDepartmentRequest;
import hufs.computer.studyroom.domain.department.dto.response.DepartmentInfoResponse;
import hufs.computer.studyroom.domain.department.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Department toDepartment(CreateDepartmentRequest request);
    void updateDepartmentFromRequest(ModifyDepartmentRequest request, @MappingTarget Department department);
    DepartmentInfoResponse toInfoResponse(Department department);

}
