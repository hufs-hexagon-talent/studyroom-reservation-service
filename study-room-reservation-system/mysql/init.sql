INSERT INTO `user` (username,login_id, password, serial, is_admin)
VALUES
    ('admin','1234','123456789','관리자', TRUE),
    ('h1009218','1234','202103769','황병훈', TRUE),
    ('0226daniel','1234','202012345','김지섭', TRUE),
    ('lsy0476','1234','202202465','이서연', FALSE),
    ('sojeong22','1234','202200720','김소정', FALSE);


INSERT INTO `roomOperationPolicy` (room_operation_policy_id,operation_start_time, operation_end_time, each_max_minute)
VALUES
    (1,'09:00:00', '17:00:00', 60),
    (2,'09:00:00', '17:00:00', 120),
    (3,'09:00:00', '17:00:00', 180),
    (4,'09:00:00', '22:00:00', 60),
    (5,'09:00:00', '22:00:00', 120),
    (6,'09:00:00', '22:00:00', 180);

INSERT INTO `room` (room_id,room_name)
VALUES
    (1,'306-1'),
    (2,'306-2'),
    (3,'306-3'),
    (4,'306-4'),
    (5,'428-1'),
    (6,'428-2');

INSERT INTO `roomOperationPolicySchedule` (room_id, room_operation_policy_id, policy_application_date)
VALUES
    (1,1,'2024-04-17'),
    (2,1,'2024-04-17'),
    (3,1,'2024-04-17'),
    (4,1,'2024-04-17'),
    (5,1,'2024-04-17'),
    (6,1,'2024-04-17');

INSERT INTO `reservation` (user_id,room_id, reservation_start_time, reservation_end_time,state)
VALUES
    (2,1,'09:00:00', '10:00:00', 'RESERVED');

