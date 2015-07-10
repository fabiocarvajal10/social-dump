--liquibase formatted sql
--changeset esteban:1436492119137-54

INSERT INTO GenericStatus
 (status, description)
VALUES
 ('Activo',   'Estado activo'),
 ('Inactivo', 'Estado inactivo');

INSERT INTO Organization
(name, ownerId, createdAt)
VALUES
('ORG1', (SELECT MIN(id) FROM JHI_USER), CURRENT_TIMESTAMP),
('ORG2', (SELECT MIN(id) FROM JHI_USER), CURRENT_TIMESTAMP);

INSERT INTO EventType
(name, description)
VALUES
('Comercial', 'Eventos comerciales');


INSERT INTO EventStatus
(status, description)
VALUES
('Activo',    'Estado Activo'),
('Cancelado', 'Estado Cancelado'),
('Pendiente', 'Estado Pendiente'),
('Inactivo',  'Estado Inactivo');

INSERT INTO Event
(organizationId, eventTypeId, startDate,
 endDate, statusId, description, activatedAt, postDelay)
VALUES
((SELECT MAX(id) FROM Organization), (SELECT MIN(id) FROM EventType),
 CURRENT_TIMESTAMP, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 60 DAY),
 (SELECT MIN(id) FROM EventStatus), 'Evento #1',
 CURRENT_TIMESTAMP, 30);

INSERT INTO SocialNetwork
(name, url)
VALUES
('Twitter', 'https://twitter.com/'),
('Instagram', 'https://instagram.com/');


INSERT INTO SocialNetworkEndpoint
(socialNetworkId, endpoint, httpMethod, description)
VALUES
((SELECT id FROM SocialNetwork WHERE name='Twitter'),
 'https://api.twitter.com/1.1/search/tweets.json', 'GET', 'search tweets');


INSERT INTO SocialNetworkApiCredential
(socialNetworkId, appId, appSecret)
VALUES
((SELECT MIN(id) FROM SocialNetwork), 'AnU9qjdTGUCEdI0QPWIteheFj',
 'UfRZCSnerq9pzIQ74g6AGX3l1LCAeEjeyV7dobYkUpQim5xLDi'),
((SELECT MIN(id) + 1 FROM SocialNetwork), '964ebd318575473c92336755cfd40124',
 'ea8f556caeba4c24978f81c09113a2f1');

INSERT INTO SearchCriteria
(eventId, searchCriteria, statusId, socialNetworkId)
VALUES
((SELECT MAX(id) FROM Event), 'IntelCR', (SELECT MIN(id) FROM GenericStatus),
 (SELECT MIN(id) FROM SocialNetwork));

COMMIT;
