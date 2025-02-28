-- 변경사항: 'user' 테이블의 'ServiceRole' 컬럼에 새로운 ENUM 값 'BLOCKED' 추가
ALTER TABLE user
    MODIFY COLUMN service_role ENUM('USER', 'ADMIN', 'RESIDENT', 'BLOCKED', 'EXPIRED') NOT NULL DEFAULT 'USER';