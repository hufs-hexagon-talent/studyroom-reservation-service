package hufs.computer.studyroom.domain.department.controller;

import hufs.computer.studyroom.common.response.SuccessResponse;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import hufs.computer.studyroom.domain.department.dto.request.CreateDepartmentRequest;
import hufs.computer.studyroom.domain.department.dto.request.ModifyDepartmentRequest;
import hufs.computer.studyroom.domain.department.dto.response.DepartmentInfoResponse;
import hufs.computer.studyroom.domain.department.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Department", description = "관리 부서 정보 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    @Operation(summary = "✅[관리자] Department 생성",
            description = "Department 생성하는 API",
            security = {@SecurityRequirement(name = "JWT")})
    @PostMapping("/partition")
    public ResponseEntity<SuccessResponse<DepartmentInfoResponse>> createDepartment(@RequestBody CreateDepartmentRequest request) {
        var result = departmentService.createDepartment(request);
        return ResponseFactory.created(result);
    }

    @Operation(summary = "✅[관리자] Department 조회",
            description = "Department 조회하는 API",
            security = {@SecurityRequirement(name = "JWT")})
    @PostMapping("/{departmentId}")
    public ResponseEntity<SuccessResponse<DepartmentInfoResponse>> getDepartmentById(@PathVariable Long departmentId) {
        var result = departmentService.findDepartmentById(departmentId);
        return ResponseFactory.success(result);
    }

    @Operation(summary = "✅[관리자] Department 정보 수정",
            description = "해당 department id의 정보 업데이트 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @PatchMapping("/{departmentId}")
    public ResponseEntity<SuccessResponse<DepartmentInfoResponse>> updateDepartment(@PathVariable Long departmentId,
                                                                                  @RequestBody ModifyDepartmentRequest request) {
        var result = departmentService.updateDepartment(departmentId, request);
        return ResponseFactory.modified(result);
    }
    @Operation(summary = "✅[관리자] Department 삭제",
            description = "해당 department id의 정보 삭제 API",
            security = {@SecurityRequirement(name = "JWT")}
    )
    @DeleteMapping("/{departmentId}")
    public ResponseEntity<SuccessResponse<Void>> deleteDepartment(@PathVariable Long departmentId) {
        departmentService.deleteDepartment(departmentId);
        return ResponseFactory.deleted();
    }
}
