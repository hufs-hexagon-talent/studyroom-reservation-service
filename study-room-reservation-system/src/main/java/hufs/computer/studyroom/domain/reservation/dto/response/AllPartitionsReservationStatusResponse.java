package hufs.computer.studyroom.domain.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "각 파티션 예약 현황 응답 목록 DTO")
public record AllPartitionsReservationStatusResponse(
        @Schema(description = "모든 파티션들의 예약 현황 응답 목록 DTO")
        List<PartitionReservationStatus> partitionReservationInfos
) {}
