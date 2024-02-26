DROP TABLE IF EXISTS `Room`;

CREATE TABLE `Room` (
	`roomId`	BIGINT	NOT NULL,
	`timetableConfigId`	BIGINT	NOT NULL,
	`name`	VARCHAR(255)	NULL
);

DROP TABLE IF EXISTS `Partition`;

CREATE TABLE `Partition` (
	`partitionId`	BIGINT	NOT NULL,
	`timetableConfigId`	BIGINT	NOT NULL,
	`roomId`	BIGINT	NOT NULL
);

DROP TABLE IF EXISTS `Reservation`;

CREATE TABLE `Reservation` (
	`reservationId`	BIGINT	NOT NULL,
	`userId`	BIGINT	NOT NULL,
	`partitionId`	BIGINT	NOT NULL,
	`timetableId`	BIGINT	NOT NULL,
	`roomId`	BIGINT	NOT NULL,
	`timetableIndexFrom`	INT	NULL,
	`timetableIndexTo`	INT	NULL,
	`state`	ENUM (  'RESERVATED', 'VISITED', 'NOSHOW' )	NULL
);

DROP TABLE IF EXISTS `User`;

CREATE TABLE `User` (
	`userId`	BIGINT	NOT NULL,
	`serial`	INT	NULL,
	`name`	VARCHAR(30)	NULL,
	`password`	VARCHAR(255)	NULL,
	`isAdmin`	BOOLEAN	NULL
);

DROP TABLE IF EXISTS `TimetableConfig`;

CREATE TABLE `TimetableConfig` (
	`timetableConfigId`	BIGINT	NOT NULL,
	`startTime`	TIME	NULL,
	`endTime`	TIME	NULL,
	`intervalMinute`	INT	NULL,
	`maxSlots`	DATE	NULL
);

DROP TABLE IF EXISTS `Timetable`;

CREATE TABLE `Timetable` (
	`timetableId`	BIGINT	NOT NULL,
	`startTime`	TIME	NULL,
	`endTime`	TIME	NULL,
	`intervalMinute`	INT	NULL,
	`maxSlots`	DATE	NULL
);

ALTER TABLE `Room` ADD CONSTRAINT `PK_ROOM` PRIMARY KEY (
	`roomId`,
	`timetableConfigId`
);

ALTER TABLE `Partition` ADD CONSTRAINT `PK_PARTITION` PRIMARY KEY (
	`partitionId`,
	`timetableConfigId`,
	`roomId`
);

ALTER TABLE `Reservation` ADD CONSTRAINT `PK_RESERVATION` PRIMARY KEY (
	`reservationId`,
	`userId`,
	`partitionId`,
	`timetableId`,
	`roomId`
);

ALTER TABLE `User` ADD CONSTRAINT `PK_USER` PRIMARY KEY (
	`userId`
);

ALTER TABLE `TimetableConfig` ADD CONSTRAINT `PK_TIMETABLECONFIG` PRIMARY KEY (
	`timetableConfigId`
);

ALTER TABLE `Timetable` ADD CONSTRAINT `PK_TIMETABLE` PRIMARY KEY (
	`timetableId`
);

ALTER TABLE `Room` ADD CONSTRAINT `FK_TimetableConfig_TO_Room_1` FOREIGN KEY (
	`timetableConfigId`
)
REFERENCES `TimetableConfig` (
	`timetableConfigId`
);

ALTER TABLE `Partition` ADD CONSTRAINT `FK_Room_TO_Partition_1` FOREIGN KEY (
	`timetableConfigId`
)
REFERENCES `Room` (
	`timetableConfigId`
);

ALTER TABLE `Partition` ADD CONSTRAINT `FK_Room_TO_Partition_2` FOREIGN KEY (
	`roomId`
)
REFERENCES `Room` (
	`roomId`
);

ALTER TABLE `Reservation` ADD CONSTRAINT `FK_User_TO_Reservation_1` FOREIGN KEY (
	`userId`
)
REFERENCES `User` (
	`userId`
);

ALTER TABLE `Reservation` ADD CONSTRAINT `FK_Partition_TO_Reservation_1` FOREIGN KEY (
	`partitionId`
)
REFERENCES `Partition` (
	`partitionId`
);

ALTER TABLE `Reservation` ADD CONSTRAINT `FK_Partition_TO_Reservation_2` FOREIGN KEY (
	`roomId`
)
REFERENCES `Partition` (
	`roomId`
);

ALTER TABLE `Reservation` ADD CONSTRAINT `FK_Timetable_TO_Reservation_1` FOREIGN KEY (
	`timetableId`
)
REFERENCES `Timetable` (
	`timetableId`
);

