
-- room Table
ALTER TABLE `room` DROP FOREIGN KEY `FK_Room_TimetableConfig`;

DROP TABLE IF EXISTS `room`;
CREATE TABLE `room` (
	`roomId` INT NOT NULL,
	`timetableConfigId` BIGINT ,
	`name`	VARCHAR(255),
	PRIMARY KEY (`roomId`)
);

-- partition Table
ALTER TABLE `partition` DROP FOREIGN KEY `FK_Partition_TimetableConfig`;
ALTER TABLE `partition` DROP FOREIGN KEY `FK_Partition_Room`;

DROP TABLE IF EXISTS `partition`;
CREATE TABLE `partition` (
	`partitionId` INT NOT NULL,
	`timetableConfigId` BIGINT	,
	`roomId` INT	,
	PRIMARY KEY (`partitionId`)
);

-- reservation Table
ALTER TABLE `reservation` DROP FOREIGN KEY `FK_Reservation_User`;
ALTER TABLE `reservation` DROP FOREIGN KEY `FK_Reservation_Partition`;
ALTER TABLE `reservation` DROP FOREIGN KEY `FK_Reservation_Room`;
ALTER TABLE `reservation` DROP FOREIGN KEY `FK_Reservation_Timetable`;

DROP TABLE IF EXISTS `reservation`;
CREATE TABLE `reservation` (
	`reservationId`	BIGINT NOT NULL AUTO_INCREMENT,
	`userId` BIGINT NOT NULL,
	`partitionId`	INT	,
	`timetableId`	BIGINT,
	`roomId`	INT	,
	`timetableIndexFrom`	INT	,
	`timetableIndexTo`	INT	,
	`state`	ENUM (  'RESERVED', 'VISITED', 'NOSHOW' ),
	PRIMARY KEY(`reservationId`)
);

-- user Table
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
	`userId` BIGINT NOT NULL, -- 학번 
	`name`	VARCHAR(30),
	`password`	VARCHAR(255),
	`isAdmin`	BOOLEAN	,
	PRIMARY KEY(`userId`)
);

-- timetableConfig Table
DROP TABLE IF EXISTS `timetableConfig`;
CREATE TABLE `timetableConfig` (
	`timetableConfigId` BIGINT NOT NULL,
	`startTime`	TIME,
	`endTime`	TIME,
	`intervalMinute`	INT	,
	`maxSlots`	DATE,
	PRIMARY KEY(`timetableConfigId`)
);

-- timetableConfig Table
DROP TABLE IF EXISTS `timetable`;
CREATE TABLE `timetable` (
	`timetableId` BIGINT NOT NULL,
	`startTime`	TIME ,
	`endTime`	TIME ,
	`intervalMinute`	INT	,
	`maxSlots`	DATE,
	PRIMARY KEY(`timetableId`)
);




ALTER TABLE `room` ADD CONSTRAINT `FK_Room_TimetableConfig` 
FOREIGN KEY (`timetableConfigId`) REFERENCES `timetableConfig` (`timetableConfigId`);

ALTER TABLE `partition` ADD CONSTRAINT `FK_Partition_TimetableConfig` 
FOREIGN KEY (`timetableConfigId`) REFERENCES `timetableConfig` (`timetableConfigId`);

ALTER TABLE `partition` ADD CONSTRAINT `FK_Partition_Room` 
FOREIGN KEY (`roomId`) REFERENCES `room` (`roomId`);


ALTER TABLE `reservation` ADD CONSTRAINT `FK_Reservation_User` 
FOREIGN KEY (`userId`) REFERENCES `user` (`userId`);

ALTER TABLE `reservation` ADD CONSTRAINT `FK_Reservation_Partition` 
FOREIGN KEY (`partitionId`) REFERENCES `partition` (`partitionId`);

ALTER TABLE `reservation` ADD CONSTRAINT `FK_Reservation_Room`
FOREIGN KEY (`roomId`) REFERENCES `room` (`roomId`);

ALTER TABLE `reservation` ADD CONSTRAINT `FK_Reservation_Timetable` 
FOREIGN KEY (`timetableId`) REFERENCES `timetable` (`timetableId`);




-- INSERT INTO `reservation` (`userId`) VALUES
--     (111),(222),(333),
--     (444),(555),(666);
