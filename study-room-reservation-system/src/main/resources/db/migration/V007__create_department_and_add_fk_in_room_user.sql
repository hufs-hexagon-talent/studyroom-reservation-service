CREATE TABLE IF NOT EXISTS `service-db`.department (
    department_id   BIGINT AUTO_INCREMENT NOT NULL,
    department_name VARCHAR(255) NULL,
    CONSTRAINT pk_department PRIMARY KEY (department_id)
);

ALTER TABLE `service-db`.room
    ADD department_id BIGINT NULL;

ALTER TABLE `service-db`.user
    ADD department_id BIGINT NULL;

ALTER TABLE `service-db`.room
    ADD CONSTRAINT FK_ROOM_ON_DEPARTMENT FOREIGN KEY (department_id) REFERENCES `service-db`.department (department_id);

ALTER TABLE `service-db`.user
    ADD CONSTRAINT FK_USER_ON_DEPARTMENT FOREIGN KEY (department_id) REFERENCES `service-db`.department (department_id);