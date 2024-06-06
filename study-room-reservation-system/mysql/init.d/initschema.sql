create table room
(
    room_id   bigint auto_increment
        primary key,
    room_name varchar(255) null
);

create table room_operation_policy
(
    each_max_minute          int     null,
    operation_end_time       time(6) null,
    operation_start_time     time(6) null,
    room_operation_policy_id bigint auto_increment
        primary key
);

create table room_operation_policy_schedule
(
    policy_application_date           date   null,
    room_id                           bigint null,
    room_operation_policy_id          bigint null,
    room_operation_policy_schedule_id bigint auto_increment
        primary key,
    constraint UKpfqbat5pgysdgfkha5fyggpi7
        unique (room_id, policy_application_date),
    constraint FK69q6uql7yr8nj2be1s7hduk8j
        foreign key (room_operation_policy_id) references room_operation_policy (room_operation_policy_id),
    constraint FKtqf4du6n3yxxy8bg6wwgyq0iq
        foreign key (room_id) references room (room_id)
);

create table user
(
    is_admin bit          null,
    user_id  bigint auto_increment
        primary key,
    serial   varchar(9)   null,
    name     varchar(255) null,
    password varchar(255) null,
    username varchar(255) null,
    constraint UK_itinxgefg8bjyffgxwwmc2oaa
        unique (serial),
    constraint UK_sb8bbouer5wak8vyiiy4pf2bx
        unique (username)
);

create table reservation
(
    reservation_end_time   datetime(6)                            null,
    reservation_id         bigint auto_increment
        primary key,
    reservation_start_time datetime(6)                            null,
    room_id                bigint                                 null,
    user_id                bigint                                 null,
    state                  enum ('NOT_VISITED', 'VISITED') null,
    constraint FKm4oimk0l1757o9pwavorj6ljg
        foreign key (user_id) references user (user_id),
    constraint FKm8xumi0g23038cw32oiva2ymw
        foreign key (room_id) references room (room_id)
);

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
