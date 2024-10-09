package hufs.computer.studyroom.domain.user.dto.response;

import hufs.computer.studyroom.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "회원 정보 응답 DTO")
public record UserInfoResponse(

        @Schema(description = "회원 Id", example = "41")
        Long userId,

        @Schema(description = "회원 Role", example = "USER")
        User.ServiceRole serviceRole,

        @Schema(description = "로그인 ID", example = "202512345")
        String username,

        @Schema(description = "학번", example = "202512345")
        String serial,

        @Schema(description = "회원 이름", example = "황병훈")
        String name,

        @Schema(description = "이메일", example = "@hufs.ac.kr")
        String email,

        @Schema(description = "소속 학과 ID", example = "1")
        Long departmentId,

        @Schema(description = "소속 학과 이름", example = "컴퓨터공학과")
        String departmentName
) {
}
