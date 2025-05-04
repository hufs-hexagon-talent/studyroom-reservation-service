package hufs.computer.studyroom.domain.department.mapper;

import hufs.computer.studyroom.domain.department.dto.request.CreateDepartmentRequest;
import hufs.computer.studyroom.domain.department.dto.request.ModifyDepartmentRequest;
import hufs.computer.studyroom.domain.department.dto.response.DepartmentInfoResponse;
import hufs.computer.studyroom.domain.department.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(target = "departmentId", ignore = true)
    Department toDepartment(CreateDepartmentRequest request);

    @Mapping(target = "departmentId", ignore = true)
    void updateDepartmentFromRequest(ModifyDepartmentRequest request, @MappingTarget Department department);
    DepartmentInfoResponse toInfoResponse(Department department);

    default List<DepartmentInfoResponse> toInfoResponseList(List<Department> departments){
        return departments.stream()
                .map(this::toInfoResponse)
                .toList();
    };
}
