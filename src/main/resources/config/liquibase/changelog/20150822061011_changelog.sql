--liquibase formatted sql
--changeset esteban:1439778630065-3

ALTER TABLE SocialNetworkPost
    CONVERT TO CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
