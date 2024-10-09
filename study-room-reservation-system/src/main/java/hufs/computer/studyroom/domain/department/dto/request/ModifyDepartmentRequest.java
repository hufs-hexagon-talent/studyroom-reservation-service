package hufs.computer.studyroom.domain.department.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "관리 부서 수정 요청 DTO")
@Builder
public record ModifyDepartmentRequest(
        @Schema(description = "관리부서명", example = "컴퓨터 공학과")
        String departmentName
) {}
