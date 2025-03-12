package hufs.computer.studyroom.domain.banner.dto.request;

import hufs.computer.studyroom.domain.banner.entity.BannerType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "배너 생성 요청 DTO")
public record CreateBannerRequest(
        @Schema(description = "배너 유형")
        @NotNull(message = "배너유형을 입력해주세요.")
        BannerType bannerType,

        @Schema(description = "배너 이미지 URL")
        @NotNull(message = "배너 이미지 URL을 입력해주세요.")
        String imageUrl,

        @Schema(description = "클릭 시 이동할 링크 URL(옵션)")
        String linkUrl

) {}
