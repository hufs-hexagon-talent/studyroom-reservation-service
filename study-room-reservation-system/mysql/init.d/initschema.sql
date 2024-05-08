create table `service-db`.room
(
    room_id   bigint auto_increment
        primary key,
    room_name varchar(255) null
);

create table `service-db`.room_operation_policy
(
    room_operation_policy_id bigint auto_increment
        primary key,
    each_max_minute          int     null,
    operation_end_time       time(6) null,
    operation_start_time     time(6) null
);

create table `service-db`.room_operation_policy_schedule
(
    room_operation_policy_schedule_id bigint auto_increment
        primary key,
    policy_application_date           date   null,
    room_id                           bigint null,
    room_operation_policy_id          bigint null,
    constraint UKpfqbat5pgysdgfkha5fyggpi7
        unique (room_id, policy_application_date),
    constraint FK69q6uql7yr8nj2be1s7hduk8j
        foreign key (room_operation_policy_id) references `service-db`.room_operation_policy (room_operation_policy_id),
    constraint FKtqf4du6n3yxxy8bg6wwgyq0iq
        foreign key (room_id) references `service-db`.room (room_id)
);

create table `service-db`.user
(
    user_id   bigint auto_increment
        primary key,
    is_admin  bit          null,
    login_id  varchar(255) null,
    password  varchar(255) null,
    serial    varchar(255) null,
    user_name varchar(255) null,
    constraint UK_6ntlp6n5ltjg6hhxl66jj5u0l
        unique (login_id),
    constraint UK_itinxgefg8bjyffgxwwmc2oaa
        unique (serial)
);

create table `service-db`.reservation
(
    reservation_id         bigint auto_increment
        primary key,
    reservation_end_time   datetime(6)                               null,
    reservation_start_time datetime(6)                               null,
    state                  enum ('RESERVATION', 'VISITED', 'NOSHOW') null,
    room_id                bigint                                    null,
    user_id                bigint                                    null,
    constraint FKm4oimk0l1757o9pwavorj6ljg
        foreign key (user_id) references `service-db`.user (user_id),
    constraint FKm8xumi0g23038cw32oiva2ymw
        foreign key (room_id) references `service-db`.room (room_id)
);

-- data 삽입
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
