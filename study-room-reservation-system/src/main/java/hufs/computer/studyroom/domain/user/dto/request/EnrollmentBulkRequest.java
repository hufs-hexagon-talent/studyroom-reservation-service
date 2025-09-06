package hufs.computer.studyroom.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

/**
 * 재학생 데이터 일괄 동기화 요청 DTO
 * 매 학기 재학생 명단을 받아 시스템 사용자 상태를 업데이트하는 용도
 */
@Builder
@Schema(description = "재학생 데이터 일괄 동기화 요청")
public record EnrollmentBulkRequest(
        @Schema(description = "재학생 정보 목록", required = true)
        @NotEmpty(message = "재학생 정보를 최소 1명 이상 입력해주세요.")
        @Valid
        List<EnrollmentRequest> enrollments
) {
}
