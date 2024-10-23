package hufs.computer.studyroom.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "회원 일괄 가입 요청 DTO")
public record SignUpBulkRequest(
        @Valid
        @Schema(description = "일괄 회원 가입 요청")
        List<SignUpRequest> signUpRequests
) {
}
