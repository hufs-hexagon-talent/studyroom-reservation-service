ALTER TABLE `service-db`.user
    ADD CONSTRAINT uc_user_email UNIQUE (email);