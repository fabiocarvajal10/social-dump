--liquibase formatted sql
--changeset esteban:1438566738285-4


ALTER TABLE SocialNetworkApiCredential ADD (
    accessToken varchar(100)
);


INSERT INTO SocialNetwork
(name, url)
VALUES
('Facebook', 'https://facebook.com/');

INSERT INTO SocialNetworkApiCredential
(socialNetworkId, appId, appSecret, accessToken)
VALUES
((SELECT id FROM SocialNetwork WHERE name='Facebook'), '1119576668057066',
 'e5387d3fe0cae3edfb023a9e3af7e72f',
 '1119576668057066|P_hEJ1mCHWegfLdm_2k_UczjTtU');

COMMIT;
