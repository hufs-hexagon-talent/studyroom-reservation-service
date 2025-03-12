package hufs.computer.studyroom.domain.banner.dto.response;


import hufs.computer.studyroom.domain.banner.entity.Banner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "배너 정보들 응답 DTO")
public record BannerInfoResponses(
        @Schema(description = "배너 정보 응답 DTO 목록")
        List<BannerInfoResponse> bannerInfoResponses
) {
}
