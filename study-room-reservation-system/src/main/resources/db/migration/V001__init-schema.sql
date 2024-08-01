CREATE TABLE reservation
(
    reservation_id         BIGINT AUTO_INCREMENT NOT NULL,
    user_id                BIGINT NULL,
    room_partition_id      BIGINT NULL,
    reservation_start_time datetime NULL,
    reservation_end_time   datetime NULL,
    state                  ENUM ('NOT_VISITED', 'VISITED') NULL,
    CONSTRAINT pk_reservation PRIMARY KEY (reservation_id)
);

CREATE TABLE room
(
    room_id   BIGINT AUTO_INCREMENT NOT NULL,
    room_name VARCHAR(255) NULL,
    CONSTRAINT pk_room PRIMARY KEY (room_id)
);

CREATE TABLE room_operation_policy
(
    room_operation_policy_id BIGINT AUTO_INCREMENT NOT NULL,
    operation_start_time     time NULL,
    operation_end_time       time NULL,
    each_max_minute          INT NULL,
    CONSTRAINT pk_roomoperationpolicy PRIMARY KEY (room_operation_policy_id)
);

CREATE TABLE room_operation_policy_schedule
(
    room_operation_policy_schedule_id BIGINT AUTO_INCREMENT NOT NULL,
    room_id                           BIGINT NULL,
    room_operation_policy_id          BIGINT NULL,
    policy_application_date           date NULL,
    CONSTRAINT pk_roomoperationpolicyschedule PRIMARY KEY (room_operation_policy_schedule_id)
);

CREATE TABLE room_partition
(
    room_partition_id BIGINT AUTO_INCREMENT NOT NULL,
    room_id           BIGINT NULL,
    partition_number  VARCHAR(255) NULL,
    CONSTRAINT pk_room_partition PRIMARY KEY (room_partition_id)
);

CREATE TABLE user
(
    user_id  BIGINT AUTO_INCREMENT NOT NULL,
    username VARCHAR(255) NULL,
    password VARCHAR(255) NULL,
    serial   VARCHAR(255) NULL,
    email    VARCHAR(255) NULL,
    name     VARCHAR(255) NULL,
    is_admin BIT(1) NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id)
);

ALTER TABLE room_operation_policy_schedule
    ADD CONSTRAINT uc_1754a63eab70fcffa86b6505a UNIQUE (room_id, policy_application_date);

ALTER TABLE user
    ADD CONSTRAINT uc_user_email UNIQUE (email);

ALTER TABLE user
    ADD CONSTRAINT uc_user_serial UNIQUE (serial);

ALTER TABLE user
    ADD CONSTRAINT uc_user_username UNIQUE (username);

ALTER TABLE reservation
    ADD CONSTRAINT FK_RESERVATION_ON_ROOM_PARTITION FOREIGN KEY (room_partition_id) REFERENCES room_partition (room_partition_id);

ALTER TABLE reservation
    ADD CONSTRAINT FK_RESERVATION_ON_USER FOREIGN KEY (user_id) REFERENCES user (user_id);

ALTER TABLE room_operation_policy_schedule
    ADD CONSTRAINT FK_ROOMOPERATIONPOLICYSCHEDULE_ON_ROOM FOREIGN KEY (room_id) REFERENCES room (room_id);

ALTER TABLE room_operation_policy_schedule
    ADD CONSTRAINT FK_ROOMOPERATIONPOLICYSCHEDULE_ON_ROOM_OPERATION_POLICY FOREIGN KEY (room_operation_policy_id) REFERENCES room_operation_policy (room_operation_policy_id);

ALTER TABLE room_partition
    ADD CONSTRAINT FK_ROOM_PARTITION_ON_ROOM FOREIGN KEY (room_id) REFERENCES room (room_id);