create table room
(
    room_id bigint auto_increment
		primary key,
    room_name varchar(255) null
);

create table room_operation_policy
(
    each_max_minute int null,
    operation_end_time time(6) null,
    operation_start_time time(6) null,
    room_operation_policy_id bigint auto_increment
		primary key
);

create table room_operation_policy_schedule
(
    policy_application_date date null,
    room_id bigint null,
    room_operation_policy_id bigint null,
    room_operation_policy_schedule_id bigint auto_increment
		primary key,
    constraint UKpfqbat5pgysdgfkha5fyggpi7
        unique (room_id, policy_application_date),
    constraint FK69q6uql7yr8nj2be1s7hduk8j
        foreign key (room_operation_policy_id) references room_operation_policy (room_operation_policy_id),
    constraint FKtqf4du6n3yxxy8bg6wwgyq0iq
        foreign key (room_id) references room (room_id)
);

create table room_partition
(
    room_id bigint null,
    room_partition_id bigint auto_increment
		primary key,
    partition_number varchar(255) null,
    constraint FK5a28dqybmm39fo9f9xlw2yh3
        foreign key (room_id) references room (room_id) on delete cascade
);

create table user
(
    is_admin bit null,
    user_id bigint auto_increment
		primary key,
    email varchar(255) null,
    name varchar(255) null,
    password varchar(255) null,
    serial varchar(255) null,
    username varchar(255) null,
    constraint UK_itinxgefg8bjyffgxwwmc2oaa
        unique (serial),
    constraint UK_ob8kqyqqgmefl0aco34akdtpe
        unique (email),
    constraint UK_sb8bbouer5wak8vyiiy4pf2bx
        unique (username)
);

create table reservation
(
    reservation_end_time datetime(6) null,
    reservation_id bigint auto_increment
		primary key,
    reservation_start_time datetime(6) null,
    room_partition_id bigint null,
    user_id bigint null,
    state enum('NOT_VISITED', 'VISITED') null,
    constraint FKm4oimk0l1757o9pwavorj6ljg
        foreign key (user_id) references user (user_id) on delete cascade ,
    constraint FKool9hau2cmx5q3qwnuj7p4656
        foreign key (room_partition_id) references room_partition (room_partition_id)
);
