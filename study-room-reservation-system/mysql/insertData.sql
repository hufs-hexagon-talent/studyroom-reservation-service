-- data 삽입
INSERT INTO `service-db`.`user`
(name, username, password, serial, is_admin)
VALUES
    ('관리자','admin', '$2a$10$8M0z/VSeWXOTO8LXTimtZeK6YGTdNNRca1nFgDAFQvZoj1ZYc2bH2', '123456789' , TRUE),
    ('황병훈','h1009218', '$2a$10$5UF4pm1p5/i73rvxVGq4z.e8y0EUKpxdJHr2e5MshaNM5kXxCOjne', '202103769',  TRUE),
    ('김지섭','0226daniel', '$2a$10$GAmvgYVVlBq3.20i/hB/AOdarXyl3a8y8BvqXhEjuTJvYWGzfAJE2', '202012345',  TRUE),
    ('이서연','lsy0476', '$2a$10$mMpahAEAKv.eJTrEhlVsvu4tFvS7VsqYvqXnBZoYZ5lvEDKmCBarS', '202202465',  TRUE),
    ('김소정','sojeong22', '$2a$10$zQ/HRLZgQF5RRH0jiDzhgeg1u5otgXpvNLx2.MKJ8EqA77US7VZzu', '202200720',  TRUE);


INSERT INTO `service-db`.`room_operation_policy` (operation_start_time, operation_end_time, each_max_minute)
VALUES
    ('09:00:00', '17:00:00', 60),
    ('09:00:00', '17:00:00', 120),
    ('09:00:00', '17:00:00', 180),
    ('09:00:00', '22:00:00', 60),
    ('09:00:00', '22:00:00', 120),
    ('09:00:00', '22:00:00', 180),
    ('00:00:00', '23:59:59', 60);

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

    (1, 7, '2024-05-15'),
    (2, 7, '2024-05-15'),
    (3, 7, '2024-05-15'),
    (4, 7, '2024-05-15'),
    (5, 7, '2024-05-15'),
    (6, 7, '2024-05-15'),

    (1, 7, '2024-05-16'),
    (2, 7, '2024-05-16'),
    (3, 7, '2024-05-16'),
    (4, 7, '2024-05-16'),
    (5, 7, '2024-05-16'),
    (6, 7, '2024-05-16');

INSERT INTO `service-db`.`reservation` (user_id, room_id, reservation_start_time, reservation_end_time, state)
VALUES
    (2, 1, '2024-05-04 09:00:00', '2024-05-11 10:00:00', 'RESERVED');
