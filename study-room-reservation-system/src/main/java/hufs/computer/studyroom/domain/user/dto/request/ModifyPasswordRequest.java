package hufs.computer.studyroom.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "비밀번호 수정 요청 DTO")
public record ModifyPasswordRequest(
        @Schema(description = "기존 비밀번호", example = "oldPassword123")
        @NotBlank(message = "기존 비밀번호를 입력해주세요.")
        String prePassword,

        @Schema(description = "새 비밀번호", example = "newPassword123")
        @NotBlank(message = "새 비밀번호를 입력해주세요.")
        String newPassword
) {
}



