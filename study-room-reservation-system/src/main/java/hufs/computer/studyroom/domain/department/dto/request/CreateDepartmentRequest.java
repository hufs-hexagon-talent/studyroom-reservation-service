package hufs.computer.studyroom.domain.department.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "관리 부서 생성 요청 DTO")
public record CreateDepartmentRequest(
        @Schema(description = "관리부서명", example = "컴퓨터 공학과")
        @NotNull(message = "관리부서 이름을 입력해주세요 ")
        String departmentName
){}
