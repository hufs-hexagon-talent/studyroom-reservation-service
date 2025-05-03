package hufs.computer.studyroom.domain.reservation.repository.projection;

public interface PartitionUsageStats {
    // 파티션 식별자
    Long getPartitionId();

    // 해당 기간 내 예약 "건수"
    Long getReservationCount();

    // 해당 기간 내 예약 "총 사용 시간(분)"
    Long getTotalReservationMinutes();
}
