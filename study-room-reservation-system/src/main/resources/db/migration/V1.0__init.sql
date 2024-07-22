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
    constraint UK_room_policy_schedule
        unique (room_id, policy_application_date),
    constraint FK_room_policy_schedule_policy
        foreign key (room_operation_policy_id) references room_operation_policy (room_operation_policy_id),
    constraint FK_room_policy_schedule_room
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
    email    varchar(255) null,
    constraint UK_user_serial
        unique (serial),
    constraint UK_user_email
        unique (email),
    constraint UK_user_username
        unique (username)
);

create table reservation
(
    reservation_end_time   datetime(6)                     null,
    reservation_id         bigint auto_increment
        primary key,
    reservation_start_time datetime(6)                     null,
    room_id                bigint                          null,
    user_id                bigint                          null,
    state                  enum ('NOT_VISITED', 'VISITED') null,
    constraint FK_reservation_user
        foreign key (user_id) references user (user_id),
    constraint FK_reservation_room
        foreign key (room_id) references room (room_id)
);
