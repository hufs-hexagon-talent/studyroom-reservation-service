-- seed data
INSERT INTO `service-db`.`user`
(name, username, password, serial, email, service_role)
VALUES
    ('관리자','admin', '$2a$10$8M0z/VSeWXOTO8LXTimtZeK6YGTdNNRca1nFgDAFQvZoj1ZYc2bH2', '123456789' ,'hwangbbang9@gmail.com', 'ADMIN'),
    ('황병훈','h1009218', '$2a$10$5UF4pm1p5/i73rvxVGq4z.e8y0EUKpxdJHr2e5MshaNM5kXxCOjne', '202103769','h1009218@hufs.ac.kr',  'ADMIN'),
    ('김지섭','0226daniel', '$2a$10$GAmvgYVVlBq3.20i/hB/AOdarXyl3a8y8BvqXhEjuTJvYWGzfAJE2', '202012345', '0226daniel@hufs.ac.kr',  'ADMIN'),
    ('이서연','lsy0476', '$2a$10$mMpahAEAKv.eJTrEhlVsvu4tFvS7VsqYvqXnBZoYZ5lvEDKmCBarS', '202202465', 'lsy0476@@hufs.ac.kr', 'ADMIN' ),
    ('RESIDENT_306','306', '$2a$10$ApVj9Nogh9mAE.w3cxzVt.QH07towwYxlLeTjuE0MUwwrSXQepeza', null, 'hwangbbang9@gmail.com', 'RESIDENT' ),
    ('RESIDENT_428','428', '$2a$10$g9y1SQon8OI8RqBUC74DguxUcIPrnKg8WHT1hRY9hd9vDPM.xvTLy', null, 'hwangbbang9@gmail.com', 'RESIDENT' );

INSERT INTO `service-db`.`room_operation_policy` (operation_start_time, operation_end_time, each_max_minute)
VALUES
    ('09:00:00', '17:00:00', 60),
    ('09:00:00', '17:00:00', 120),
    ('09:00:00', '17:00:00', 180),
    ('09:00:00', '22:00:00', 60),
    ('09:00:00', '22:00:00', 120),
    ('09:00:00', '22:00:00', 180),
    ('09:00:00', '23:59:59', 60);

INSERT INTO `service-db`.`room` (room_name)
VALUES
    ('306'),
    ('428');
INSERT INTO `service-db`.`room_partition` (room_id, partition_number)
VALUES
    (1,'1'),
    (1,'2'),
    (1,'3'),
    (1,'4'),
    (2,'1'),
    (2,'2');