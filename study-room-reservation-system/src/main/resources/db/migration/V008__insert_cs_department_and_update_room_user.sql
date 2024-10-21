-- 1. 컴퓨터공학과 데이터 삽입
INSERT INTO `service-db`.department (department_name) VALUES ('컴퓨터공학');

-- 1-2. 기존 room 데이터 업데이트 (room_id가 1, 2인 경우 1번 부서 할당)
UPDATE `service-db`.room
SET department_id = 1
WHERE room_id IN (1, 2);

-- 1-3. 기존 user 데이터 업데이트 (user_id가 10부터 441까지인 경우 1번 부서 할당)
UPDATE `service-db`.user
SET department_id = 1
WHERE user_id BETWEEN 10 AND 441;