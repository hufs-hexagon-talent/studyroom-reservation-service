package hufs.computer.studyroom.domain.department.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "관리부서 정보 응답 DTO")
public record DepartmentInfoResponse(
        @Schema(description = "관리부서 ID", example = "1") Long departmentId,
        @Schema(description = "관리부서명 ", example = "컴퓨터 공학과")String departmentName
) {}
