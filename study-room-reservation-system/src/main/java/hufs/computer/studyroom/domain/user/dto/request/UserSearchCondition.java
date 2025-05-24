package hufs.computer.studyroom.domain.user.dto.request;

import hufs.computer.studyroom.domain.user.entity.ServiceRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "회원 정보 조회 조건 요청 DTO")
public record UserSearchCondition(
        @Schema(description = "로그인 아이디 (username)", example = "202512345")
        String username,
        @Schema(description = "학번 (serial)", example = "202512345")
        String serial,
        @Schema(description = "사용자 이름", example = "황병훈")
        String name,
        @Schema(description = "이메일 주소", example = "user@hufs.ac.kr")
        String email,
        @Schema(description = "회원 권한", example = "[\"USER\",\"BLOCKED\"]")
        List<ServiceRole> role,
        @Schema(description = "부서 ID", example = "1")
        Long departmentId,

        @Schema(description = "페이지 번호 (0부터 시작)", example = "0")
        Integer page,
        @Schema(description = "페이지 크기 (1~100)", example = "20")
        Integer size
//        @Schema(description = "정렬 대상 필드", example = "username")
//        String sortBy,
//        @Schema(description = "정렬 방향", example = "DESC")
//        SortDirection order
) {
}
