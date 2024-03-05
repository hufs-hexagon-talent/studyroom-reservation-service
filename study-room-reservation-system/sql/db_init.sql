DROP TABLE IF EXISTS `timetable`;
DROP TABLE IF EXISTS `timetableconfig`;
DROP TABLE IF EXISTS `user` ;
DROP TABLE IF EXISTS `reservation`;
DROP TABLE IF EXISTS `partition`;
DROP TABLE IF EXISTS `room`;
-- room Table

CREATE TABLE `room` (
	`room_id` INT NOT NULL,
	`timetable_config_id` BIGINT NOT NULL,
	`name` VARCHAR(255),
	PRIMARY KEY (`room_id`)
);

-- partition Table


CREATE TABLE `partition`(
	`partition_id` INT NOT NULL,
	`timetable_config_id` BIGINT	,
	`room_id` INT	,
	PRIMARY KEY (`partition_id`)
);

-- reservation Table

CREATE TABLE `reservation` (
	`reservation_id` BIGINT NOT NULL AUTO_INCREMENT,
	`user_id` INT NOT NULL,
	`partition_id` INT	,
	`timetable_id`	BIGINT,
	`room_id`	INT	,
	`timetable_index_from`	INT	,
	`timetable_index_to`	INT	,
	`state` ENUM ('RESERVED', 'VISITED', 'NOSHOW' ),
	PRIMARY KEY(`reservation_id`)
);

-- user Table

CREATE TABLE `user` (
	`user_id` INT NOT NULL, -- 테스트용 학번 
	`username`	VARCHAR(30) UNIQUE, 
	`password`	VARCHAR(255),
	`serial` CHAR(20) UNIQUE,
	`is_admin`	BOOLEAN	,
	PRIMARY KEY(`user_id`)
);

-- timetableConfig Table

CREATE TABLE `timetableconfig` (
	`timetable_config_id` BIGINT NOT NULL,
	`start_time` TIME,
	`end_time` TIME,
	`interval_minute`	INT	,
	`max_slots` INT,
	PRIMARY KEY(`timetable_config_id`)
);

-- timetableConfig Table

CREATE TABLE `timetable` (
	`timetable_id` BIGINT NOT NULL,
	`partition_id` INT	,
	`start_time`	TIME ,
	`end_time`	TIME ,
	`interval_minute`	INT	,
	`max_slots`	INT,
	PRIMARY KEY(`timetable_id`)
);



ALTER TABLE `room` ADD CONSTRAINT FK_timetableconfig_room FOREIGN KEY (`timetable_config_id`)
REFERENCES `timetableconfig` (`timetable_config_id`);

ALTER TABLE `partition` ADD CONSTRAINT FK_room_part FOREIGN KEY (`room_id`)
REFERENCES `room` (`room_id`);

ALTER TABLE `reservation` ADD CONSTRAINT FK_user_reservation FOREIGN KEY (`user_id`) 
REFERENCES `user` (`user_id`);

ALTER TABLE `reservation` ADD CONSTRAINT FK_part_reservation FOREIGN KEY (`partition_id`)
REFERENCES `partition` (`partition_id`);

ALTER TABLE `reservation` ADD CONSTRAINT FK_timetable_reservation FOREIGN KEY (`timetable_id`)
REFERENCES `timetable` (`timetable_id`);

ALTER TABLE `timetable` ADD CONSTRAINT FK_part_timetable FOREIGN KEY (`partition_id`)
REFERENCES `partition` (`partition_id`);

