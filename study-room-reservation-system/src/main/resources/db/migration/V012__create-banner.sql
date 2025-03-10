CREATE TABLE IF NOT EXISTS `service-db`.banner (
    banner_id   BIGINT AUTO_INCREMENT NOT NULL,
    banner_type ENUM ('AD', 'NOTICE') NOT NULL,
    image_url   VARCHAR(255) NULL,
    link_url    VARCHAR(255) NULL,
    active      TINYINT(1) NOT NULL DEFAULT 1,
    create_at   datetime NULL,
    update_at   datetime NULL,
    CONSTRAINT pk_banner PRIMARY KEY (banner_id)
);
