
-- Room Table
ALTER TABLE `Room` DROP FOREIGN KEY `FK_Room_TimetableConfig`;

DROP TABLE IF EXISTS `Room`;
CREATE TABLE `Room` (
	`roomId` INT NOT NULL,
	`timetableConfigId` BIGINT ,
	`name`	VARCHAR(255),
	PRIMARY KEY (`roomId`)
);

-- Partition Table
ALTER TABLE `Partition` DROP FOREIGN KEY `FK_Partition_TimetableConfig`;
ALTER TABLE `Partition` DROP FOREIGN KEY `FK_Partition_Room`;

DROP TABLE IF EXISTS `Partition`;
CREATE TABLE `Partition` (
	`partitionId` INT NOT NULL,
	`timetableConfigId` BIGINT	,
	`roomId` INT	,
	PRIMARY KEY (`partitionId`)
);

-- Reservation Table
ALTER TABLE `Reservation` DROP FOREIGN KEY `FK_Reservation_User`;
ALTER TABLE `Reservation` DROP FOREIGN KEY `FK_Reservation_Partition`;
ALTER TABLE `Reservation` DROP FOREIGN KEY `FK_Reservation_Room`;
ALTER TABLE `Reservation` DROP FOREIGN KEY `FK_Reservation_Timetable`;

DROP TABLE IF EXISTS `Reservation`;
CREATE TABLE `Reservation` (
	`reservationId`	BIGINT NOT NULL AUTO_INCREMENT,
	`userId` INT NOT NULL,
	`partitionId`	INT	,
	`timetableId`	BIGINT,
	`roomId`	INT	,
	`timetableIndexFrom`	INT	,
	`timetableIndexTo`	INT	,
	`state`	ENUM (  'RESERVED', 'VISITED', 'NOSHOW' ),
	PRIMARY KEY(`reservationId`)
);

-- User Table
DROP TABLE IF EXISTS `User`;
CREATE TABLE `User` (
	`userId` BIGINT NOT NULL, -- 학번 
	`name`	VARCHAR(30),
	`password`	VARCHAR(255),
	`isAdmin`	BOOLEAN	,
	PRIMARY KEY(`userId`)
);

-- TimetableConfig Table
DROP TABLE IF EXISTS `TimetableConfig`;
CREATE TABLE `TimetableConfig` (
	`timetableConfigId` BIGINT NOT NULL,
	`startTime`	TIME,
	`endTime`	TIME,
	`intervalMinute`	INT	,
	`maxSlots`	DATE,
	PRIMARY KEY(`timetableConfigId`)
);

-- TimetableConfig Table
DROP TABLE IF EXISTS `Timetable`;
CREATE TABLE `Timetable` (
	`timetableId` BIGINT NOT NULL,
	`startTime`	TIME ,
	`endTime`	TIME ,
	`intervalMinute`	INT	,
	`maxSlots`	DATE,
	PRIMARY KEY(`timetableId`)
);




ALTER TABLE `Room` ADD CONSTRAINT `FK_Room_TimetableConfig` 
FOREIGN KEY (`timetableConfigId`) REFERENCES `TimetableConfig` (`timetableConfigId`);

ALTER TABLE `Partition` ADD CONSTRAINT `FK_Partition_TimetableConfig` 
FOREIGN KEY (`timetableConfigId`) REFERENCES `TimetableConfig` (`timetableConfigId`);

ALTER TABLE `Partition` ADD CONSTRAINT `FK_Partition_Room` 
FOREIGN KEY (`roomId`) REFERENCES `Room` (`roomId`);


ALTER TABLE `Reservation` ADD CONSTRAINT `FK_Reservation_User` 
FOREIGN KEY (`userId`) REFERENCES `User` (`userId`);

ALTER TABLE `Reservation` ADD CONSTRAINT `FK_Reservation_Partition` 
FOREIGN KEY (`partitionId`) REFERENCES `Partition` (`partitionId`);

ALTER TABLE `Reservation` ADD CONSTRAINT `FK_Reservation_Room`
FOREIGN KEY (`roomId`) REFERENCES `Room` (`roomId`);

ALTER TABLE `Reservation` ADD CONSTRAINT `FK_Reservation_Timetable` 
FOREIGN KEY (`timetableId`) REFERENCES `Timetable` (`timetableId`);




-- INSERT INTO `Reservation` (`userId`) VALUES
--     (111),(222),(333),
--     (444),(555),(666);
