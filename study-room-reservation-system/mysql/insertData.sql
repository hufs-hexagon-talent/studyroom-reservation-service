INSERT INTO `service-db`.`user`
(user_name, login_id, password, serial, is_admin)
VALUES
    ('관리자','admin', 'admin', '123456789' , TRUE),
    ('황병훈','h1009218', 'h1009218', '202103769',  TRUE),
    ('김지섭','0226daniel', '0226daniel', '202012345',  TRUE),
    ('이서연','lsy0476', 'lsy0476', '202202465',  FALSE),
    ('김소정','sojeong22', 'sojeong22', '202200720',  FALSE);

INSERT INTO `service-db`.`room_operation_policy` (operation_start_time, operation_end_time, each_max_minute)
VALUES
    ('09:00:00', '17:00:00', 60),
    ('09:00:00', '17:00:00', 120),
    ('09:00:00', '17:00:00', 180),
    ('09:00:00', '22:00:00', 60),
    ('09:00:00', '22:00:00', 120),
    ('09:00:00', '22:00:00', 180);

INSERT INTO `service-db`.`room` (room_name)
VALUES
    ('306-1'),
    ('306-2'),
    ('306-3'),
    ('306-4'),
    ('428-1'),
    ('428-2');

INSERT INTO `service-db`.`room_operation_policy_schedule` (room_id, room_operation_policy_id, policy_application_date)
VALUES
    (1, 1, '2024-04-17'),
    (2, 1, '2024-04-17'),
    (3, 1, '2024-04-17'),
    (4, 1, '2024-04-17'),
    (5, 1, '2024-04-17'),
    (6, 1, '2024-04-17');

INSERT INTO `service-db`.`reservation` (user_id, room_id, reservation_start_time, reservation_end_time, state)
VALUES
    (2, 1, '2024-05-04 09:00:00', '2024-05-04 10:00:00', 'RESERVED');
