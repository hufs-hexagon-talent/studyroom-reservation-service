package hufs.computer.studyroom.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "이메일 수정 요청 DTO")
public record ModifyEmailRequest(
        @Schema(description = "현재 비밀번호", example = "user_password123")
        @NotBlank(message = "현재 비밀번호를 입력해주세요.")
        String password,

        @Schema(description = "새 이메일", example = "123456@hufs.ac.kr")
        @NotBlank(message = "새 이메일을 입력해주세요.")
        String newEmail
) {
}