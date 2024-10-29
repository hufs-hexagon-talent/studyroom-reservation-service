-- 1. 컴퓨터공학과 데이터 삽입
INSERT INTO `service-db`.department (department_name) VALUES ('컴퓨터공학');

-- 1-2. 기존 room 데이터 업데이트 (room_id가 1, 2인 경우 1번 부서 할당)
UPDATE `service-db`.room
SET department_id = 1
WHERE room_id IN (1, 2);
-- User는 수동 업데이트