create table `hufs-reservation-db`.room
(
    room_id   bigint auto_increment
        primary key,
    room_name varchar(255) null
);

create table `hufs-reservation-db`.room_operation_policy
(
    room_operation_policy_id bigint auto_increment
        primary key,
    each_max_minute          int     null,
    operation_end_time       time(6) null,
    operation_start_time     time(6) null
);

create table `hufs-reservation-db`.room_operation_policy_schedule
(
    room_operation_policy_schedule_id bigint auto_increment
        primary key,
    policy_application_date           date   null,
    room_id                           bigint null,
    room_operation_policy_id          bigint null,
    constraint UKpfqbat5pgysdgfkha5fyggpi7
        unique (room_id, policy_application_date),
    constraint FK69q6uql7yr8nj2be1s7hduk8j
        foreign key (room_operation_policy_id) references `hufs-reservation-db`.room_operation_policy (room_operation_policy_id),
    constraint FKtqf4du6n3yxxy8bg6wwgyq0iq
        foreign key (room_id) references `hufs-reservation-db`.room (room_id)
);

create table `hufs-reservation-db`.user
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

create table `hufs-reservation-db`.reservation
(
    reservation_id         bigint auto_increment
        primary key,
    reservation_end_time   datetime(6)                               null,
    reservation_start_time datetime(6)                               null,
    state                  enum ('RESERVATION', 'VISITED', 'NOSHOW') null,
    room_id                bigint                                    null,
    user_id                bigint                                    null,
    constraint FKm4oimk0l1757o9pwavorj6ljg
        foreign key (user_id) references `hufs-reservation-db`.user (user_id),
    constraint FKm8xumi0g23038cw32oiva2ymw
        foreign key (room_id) references `hufs-reservation-db`.room (room_id)
);

