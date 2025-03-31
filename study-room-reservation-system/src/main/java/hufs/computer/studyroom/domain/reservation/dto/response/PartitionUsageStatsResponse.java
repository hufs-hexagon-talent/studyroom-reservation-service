package hufs.computer.studyroom.domain.reservation.dto.response;

public record PartitionUsageStatsResponse(
        Long partitionId,
        Long reservationCount,
        Long totalReservationMinutes
) {
}
