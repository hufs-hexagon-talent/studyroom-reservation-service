package hufs.computer.studyroom.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "회원 가입 요청 DTO")
public record SignUpRequest(
        @Schema(description = "로그인 ID", example = "202512345")
        @NotNull(message = "로그인 ID를 입력해주세요.")
        String username,

        @Schema(description = "로그인 PW", example = "202512345")
        @NotNull(message = "로그인 PW를 입력해주세요.")
        String password,

        @Schema(description = "학번", example = "202512345")
//        @NotNull(message = "학번을 입력해주세요.") 학번 Null 가능 ..Resident 때문에
        String serial,

        @Schema(description = "회원 이름", example = "황병훈")
        @NotNull(message = "로그인 이름을 입력해주세요.")
        String name,

        @Email(message = "유효한 이메일 주소를 입력하세요")
        @Schema(description = "이메일", example = "@hufs.ac.kr")
        String email,

        @Schema(description = "소속 학과 ID", example = "1")
        Long departmentId
) {
}
