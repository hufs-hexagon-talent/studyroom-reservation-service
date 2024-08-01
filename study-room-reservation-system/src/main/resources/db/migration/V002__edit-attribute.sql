ALTER TABLE `service-db`.user
    ADD service_role ENUM ('USER', 'ADMIN', 'RESIDENT') NULL;
ALTER TABLE `service-db`.user
    MODIFY service_role ENUM('USER', 'ADMIN', 'RESIDENT') NOT NULL DEFAULT 'USER';

ALTER TABLE `service-db`.user
DROP
COLUMN is_admin;
