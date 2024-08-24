-- 변경사항: 'reservation' 테이블의 'state' 컬럼에 새로운 ENUM 값 'PROCESSED' 추가
ALTER TABLE reservation
    MODIFY COLUMN state ENUM('NOT_VISITED', 'VISITED', 'PROCESSED');