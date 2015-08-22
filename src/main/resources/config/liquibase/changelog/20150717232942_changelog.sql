--liquibase formatted sql
--changeset esteban:1436993305726-15

INSERT INTO EventType
(description, name)
VALUES
('Público, visible abiertamente', 'Comercial'),
('Privado, no visible abiertamente', 'Personal')
ON DUPLICATE KEY UPDATE description = VALUES(description);

COMMIT;
