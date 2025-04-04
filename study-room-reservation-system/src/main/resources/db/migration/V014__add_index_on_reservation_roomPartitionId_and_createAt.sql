-- 예약 테이블에 create_at 인덱스 추가
ALTER TABLE reservation
    ADD INDEX idx_reservation_partition_create_at (room_partition_id, create_at);