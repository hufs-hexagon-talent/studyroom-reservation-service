package hufs.computer.studyroom.domain.user.dto.request;

import hufs.computer.studyroom.domain.user.entity.User.ServiceRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
@Schema(description = "회원 정보 업데이트 요청 DTO")
public record ModifyUserInfoRequest(
        @Schema(description = "로그인 ID", example = "202512345")
        String username,

        @Schema(description = "학번", example = "202512345")
        String serial,

        @Schema(description = "회원 역할", example = "USER")
        ServiceRole serviceRole,

        @Schema(description = "회원 이름", example = "황병훈")
        String name,

        @Email(message = "유효한 이메일 주소를 입력하세요")
        @Schema(description = "이메일", example = "@hufs.ac.kr")
        String email,

        @Schema(description = "소속 학과 ID", example = "1")
        Long departmentId
) {
}
