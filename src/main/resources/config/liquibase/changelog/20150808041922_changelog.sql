--liquibase formatted sql
--changeset esteban:1439029152450-4

UPDATE SocialNetwork
SET    statusId = (SELECT id
                   FROM   GenericStatus
                   WHERE  status = 'Activo');

UPDATE SocialNetwork
SET    statusId = (SELECT id
                   FROM   GenericStatus
                   WHERE  status = 'Inactivo')
WHERE  name = 'Facebook';

COMMIT;