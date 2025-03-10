package hufs.computer.studyroom.domain.banner.dto.response;

import hufs.computer.studyroom.domain.banner.entity.BannerType;
import io.swagger.v3.oas.annotations.media.Schema;


import java.time.Instant;

public record BannerInfoResponse(
    @Schema(description = "배너 ID", example = "2")
     Long bannerId,

    @Schema(description = "배너 유형", example = "NOTICE")
     BannerType bannerType,

    @Schema(description = "배너 이미지 URL")
     String imageUrl,

    @Schema(description = "클릭 시 이동할 링크 URL")
     String linkUrl,

    @Schema(description = "배너 활성화 여부")
    boolean active,

    // 엔티티가 처음 생성될 때 자동으로 설정되는 필드
    @Schema(description = "배너 생성 시간",example = "")
     Instant createAt,

    // 엔티티가 생성되거나 수정될 때 자동으로 설정되는 필드
    @Schema(description = "배너 수정 시간",example = "")
     Instant updateAt

) {
}
