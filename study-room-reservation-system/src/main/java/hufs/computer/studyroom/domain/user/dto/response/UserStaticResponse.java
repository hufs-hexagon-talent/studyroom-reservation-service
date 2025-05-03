package hufs.computer.studyroom.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "현재 유저 통계 응답 DTO")
public record UserStaticResponse(
        @Schema(description = "전체 유저 수", example = "620")
        long totalUserCount,

        @Schema(description = "활성(Active) 유저 수", example = "420")
        long activeUserCount,        // serviceRole = USER 등

        @Schema(description = "차단(BLOCKED) 유저 수", example = "10")
        long bannedUserCount,        // serviceRole = BLOCKED

        @Schema(description = "만료(EXPIRED) 유저 수", example = "5")
        long expiredUserCount,       // serviceRole = EXPIRED

        @Schema(description = "관리자(ADMIN) 유저 수", example = "3")
        long adminUserCount,         // serviceRole = ADMIN

        @Schema(description = "시스템(RESIDENT) 유저 수", example = "2")
        long systemUserCount         // serviceRole = RESIDENT
) {
}
