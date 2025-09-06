package hufs.computer.studyroom.domain.user.dto.request;

import hufs.computer.studyroom.common.validation.annotation.ExistDepartment;
import hufs.computer.studyroom.common.validation.annotation.user.UniqueEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * 재학생 데이터 동기화용 개별 학생 정보 DTO
 * 회원가입과 달리 password, email 등이 필요없고 학번, 이름, 학과 정보만 필요
 */
@Builder
@Schema(description = "재학생 정보 DTO")
public record EnrollmentRequest(
        @Schema(description = "학번", example = "202512345", required = true)
        @NotBlank(message = "학번을 입력해주세요.")
        String serial,

        @Schema(description = "학생 이름", example = "홍길동", required = true)
        @NotBlank(message = "이름을 입력해주세요.")
        String name,

        @Schema(description = "소속 학과 ID", example = "1", required = true)
        @NotNull(message = "학과 ID를 입력해주세요.")
        @ExistDepartment
        Long departmentId,

        @Schema(description = "이메일", example = "@hufs.ac.kr")
        @Email(message = "유효한 이메일 주소를 입력하세요")
        String email
) {
    /**
     * SignUpRequest로 변환 (신규 사용자 등록 시 사용)
     * 학번을 username과 초기 password로 사용
     */
    public SignUpRequest toSignUpRequest() {
        return SignUpRequest.builder()
                .username(this.serial)  // 학번을 username으로 사용
                .password(this.serial)  // 초기 비밀번호도 학번으로 설정
                .serial(this.serial)
                .name(this.name)
                .email(this.email)  // 이메일은 나중에 본인이 등록
                .departmentId(this.departmentId)
                .build();
    }
}
