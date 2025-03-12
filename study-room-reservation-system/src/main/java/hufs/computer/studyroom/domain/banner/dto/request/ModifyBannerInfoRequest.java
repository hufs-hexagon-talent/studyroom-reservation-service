package hufs.computer.studyroom.domain.banner.dto.request;

import hufs.computer.studyroom.domain.banner.entity.BannerType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Builder;

@Builder
@Schema(description = "배너 정보 업데이트 요청 DTO")
public record ModifyBannerInfoRequest(

        @Schema(description = "배너 유형", example = "NOTICE")
        BannerType bannerType,

        @Schema(description = "배너 이미지 URL", example = "www.hwangbbang.com")
        String imageUrl,

        @Schema(description = "클릭 시 이동할 링크 URL", example = "www.hwangbbang.com")
        @Nullable
        String linkUrl,

        @Schema(description = "배너 활성화 여부", example = "true")
        boolean active
) {
}
