USE socialdump;

CREATE TABLE `JHI_AUTHORITY` (
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `JHI_PERSISTENT_AUDIT_EVENT` (
  `event_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `principal` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `event_date` timestamp NULL DEFAULT NULL,
  `event_type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`event_id`),
  KEY `idx_persistent_audit_event` (`principal`,`event_date`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `JHI_PERSISTENT_AUDIT_EVT_DATA` (
  `event_id` bigint(20) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `value` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`event_id`,`name`),
  KEY `idx_persistent_audit_evt_data` (`event_id`),
  CONSTRAINT `fk_evt_pers_audit_evt_data`
    FOREIGN KEY (`event_id`) REFERENCES `JHI_PERSISTENT_AUDIT_EVENT`(`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `JHI_USER` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `PASSWORD` varchar(60) COLLATE utf8_unicode_ci DEFAULT NULL,
  `first_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `activated` bit(1) NOT NULL,
  `lang_key` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  `activation_key` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `reset_key` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created_by` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `created_date` timestamp NOT NULL,
  `reset_date` timestamp NULL DEFAULT NULL,
  `last_modified_by` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login` (`login`),
  UNIQUE KEY `idx_user_login` (`login`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `idx_user_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `JHI_USER_AUTHORITY` (
  `user_id` bigint(20) NOT NULL,
  `authority_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`user_id`,`authority_name`),
  KEY `fk_authority_name` (`authority_name`),
  CONSTRAINT `fk_authority_name` FOREIGN KEY (`authority_name`) REFERENCES `JHI_AUTHORITY` (`name`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `JHI_USER` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO JHI_AUTHORITY
 (name)
VALUES
 ('ROLE_ADMIN'), ('ROLE_USER');

INSERT INTO JHI_USER
(id, login, `PASSWORD`, first_name, last_name, email, activated, lang_key,
 created_by)
VALUES
(1, 'system', '$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG',
 'System', 'System', 'system@localhost', 1, 'en', 'system'),
(2, 'anonymousUser',
 '$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO',
 'Anonymous', 'User', 'anonymous@localhost', 1, 'en', 'system'),
(3, 'admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC',
 'Administrator', 'Administrator', 'admin@localhost', 1, 'en', 'system'),
(4, 'user', '$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K',
 'User', 'User', 'user@localhost', 1, 'en', 'system');

INSERT INTO JHI_USER_AUTHORITY
(user_id, authority_name)
VALUES
(1, 'ROLE_ADMIN'),
(1, 'ROLE_USER'),
(3, 'ROLE_ADMIN'),
(3, 'ROLE_USER'),
(4, 'ROLE_USER');


/*                               GENERAL MODULE                               */

CREATE TABLE GenericStatus (
    id smallint(3) unsigned auto_increment PRIMARY KEY,
    status varchar(30) NOT NULL unique,
    description varchar(80) NOT NULL
);


/*                             SECURITY MODULE                                */

CREATE TABLE TemporalAccess (
    id bigint(15) unsigned auto_increment PRIMARY KEY,
    email varchar(255) NOT NULL,
    password varchar(80) NOT NULL,
    monitorContactId bigint(15) unsigned,
    createdAt timestamp NOT NULL,
    startDate timestamp NOT NULL,
    endDate timestamp NOT NULL,
    statusId smallint(3) unsigned,
    eventId bigint(15) unsigned,
    CONSTRAINT tempaccess_status_fk FOREIGN KEY (statusId)
        REFERENCES GenericStatus(id)
);




/*                           ORGANIZATIONS MODULE                             */

CREATE TABLE OrganizationFuncionality (
    id int(4) unsigned auto_increment PRIMARY KEY,
    name varchar(80) NOT NULL
);

CREATE TABLE OrganizationRole(
    id smallint(3) unsigned auto_increment PRIMARY KEY,
    name varchar(50) NOT NULL unique
);

CREATE TABLE OrganizationPrivilege (
    id int(7) unsigned auto_increment PRIMARY KEY,
    functionalityId  int(4) unsigned NOT NULL,
    roleId smallint(3) unsigned NOT NULL,
    CONSTRAINT orgprivilege_func_fk FOREIGN KEY (functionalityId)
        REFERENCES OrganizationFuncionality(id),
    CONSTRAINT orgprivilege_role_fk FOREIGN KEY (roleId)
        REFERENCES OrganizationRole(id)
);

CREATE TABLE Organization (
    id bigint(15) unsigned auto_increment PRIMARY KEY,
    name varchar(100) NOT NULL,
    createdAt timestamp default current_timestamp,
    ownerId bigint(20) NOT NULL,
    CONSTRAINT organization_user_fk FOREIGN KEY (ownerId)
        REFERENCES JHI_USER(id)
);

CREATE TABLE OrganizationMember (
    id bigint(15) unsigned auto_increment PRIMARY KEY,
    userId bigint(20) NOT NULL,
    roleId smallint(3) unsigned NOT NULL,
    organizationId  bigint(15) unsigned NOT NULL,
    statusId smallint(3) unsigned NOT NULL,
    startDate timestamp NOT NULL,
    endDate timestamp,
    CONSTRAINT orgmember_user_fk FOREIGN KEY (userId) REFERENCES JHI_USER(id),
    CONSTRAINT orgmember_role_fk FOREIGN KEY (roleId)
        REFERENCES OrganizationRole(id),
    CONSTRAINT orgmember_organization_fk FOREIGN KEY (organizationId)
        REFERENCES Organization(id),
    CONSTRAINT orgmember_status_fk FOREIGN KEY (statusId)
        REFERENCES GenericStatus(id)
);

CREATE TABLE MonitorContact (
    id  bigint(15) unsigned auto_increment PRIMARY KEY,
    firstName varchar(100) NOT NULL,
    lastName varchar(100) NOT NULL,
    email varchar(255),
    phone varchar(80),
    organizationId bigint(15) unsigned NOT NULL,
    CONSTRAINT monitorcontact_org_fk FOREIGN KEY (organizationId)
        REFERENCES Organization(id)
);

CREATE TABLE EventType (
    id int(4) unsigned auto_increment PRIMARY KEY,
    name varchar(50) NOT NULL,
    description varchar(100) NOT NULL
);

CREATE TABLE EventStatus (
    id smallint(3) unsigned auto_increment PRIMARY KEY,
    status  varchar(30) NOT NULL unique,
    description varchar(80) NOT NULL
);

CREATE TABLE Event (
    id bigint(15) unsigned auto_increment PRIMARY KEY,
    organizationId bigint(15) unsigned NOT NULL,
    eventTypeId int(4) unsigned NOT NULL,
    startDate timestamp NOT NULL,
    endDate timestamp NOT NULL,
    statusId smallint(3) unsigned NOT NULL,
    description varchar(255) NOT NULL,
    activatedAt timestamp,
    postDelay int(6) unsigned NOT NULL,
    CONSTRAINT event_organization_fk FOREIGN KEY (organizationId)
        REFERENCES Organization(id),
    CONSTRAINT event_type_fk FOREIGN KEY (eventTypeId) REFERENCES EventType(id),
    CONSTRAINT event_status_fk FOREIGN KEY (statusId) REFERENCES EventStatus(id)
);

ALTER TABLE TemporalAccess ADD (
    CONSTRAINT tempaccess_monitor_fk FOREIGN KEY (monitorContactId)
        REFERENCES MonitorContact(id),
    CONSTRAINT tempaccess_event_fk FOREIGN KEY (eventId) REFERENCES Event(id)
);

CREATE TABLE SearchCriteria (
    id  bigint(15) unsigned auto_increment PRIMARY KEY,
    eventId bigint(15) unsigned NOT NULL,
    searchCriteria varchar(255) NOT NULL,
    statusId smallint(3) unsigned NOT NULL,
    socialNetworkId int(4) unsigned NOT NULL,
    CONSTRAINT searchcriteria_event_fk FOREIGN KEY (eventId)
        REFERENCES Event(id),
    CONSTRAINT searchcriteria_status_fk FOREIGN KEY (statusId)
        REFERENCES GenericStatus(id)
);



/*                         SOCIAL NETWORKS MODULE                             */

CREATE TABLE SocialNetwork (
    id int(4) unsigned auto_increment PRIMARY KEY,
    name varchar(80) NOT NULL unique,
    url varchar(250) NOT NULL
);

ALTER TABLE SearchCriteria ADD
    CONSTRAINT searchcriteria_socnet_fk FOREIGN KEY (socialNetworkId)
        REFERENCES SocialNetwork(id);

CREATE TABLE SocialNetworkEndpoint (
    id int(5) unsigned auto_increment PRIMARY KEY,
    socialNetworkId int(4) unsigned NOT NULL,
    endpoint varchar(255) NOT NULL,
    httpMethod varchar(10) NOT NULL,
    description varchar(100),
    CONSTRAINT socialnetwork_endpoint_fk FOREIGN KEY (socialNetworkId)
        REFERENCES SocialNetwork(id)
);

CREATE TABLE SocialNetworkRequest (
    id bigint(15) unsigned auto_increment PRIMARY KEY,
    targetUrl varchar(255),
    endpointId int(5) unsigned,
    eventId bigint(15) unsigned,
    request text,
    searchCriteriaId bigint(15) unsigned,
    searchCriteria varchar(255),
    createdAt timestamp NOT NULL,
    CONSTRAINT socialnetworkendpoint_req_fk FOREIGN KEY (endpointId)
        REFERENCES SocialNetworkEndpoint(id),
    CONSTRAINT socialnetreq_event_fk FOREIGN KEY (eventId) REFERENCES Event(id),
    CONSTRAINT socialnetreq_searchc_fk FOREIGN KEY (searchCriteriaId)
        REFERENCES SearchCriteria(id)
);

CREATE TABLE SocialNetworkResponse (
    id bigint(15) unsigned auto_increment PRIMARY KEY,
    requestId bigint(15) unsigned,
    status smallint(3) NOT NULL,
    headers text,
    body text,
    CONSTRAINT socialnet_req_resp_fk FOREIGN KEY (requestId)
        REFERENCES SocialNetworkRequest(id)
);

CREATE TABLE SocialNetworkPost (
    id bigint(15) unsigned auto_increment PRIMARY KEY,
    requestId bigint(15) unsigned,
    responseId bigint(15) unsigned,
    eventId bigint(15) unsigned,
    searchCriteriaId bigint(15) unsigned,
    socialNetworkId int(4) unsigned,
    createdAt timestamp NOT NULL,
    snUserId bigint(20) unsigned,
    snUserEmail varchar(255),
    body text,
    mediaUrl varchar(255),
    statusId smallint(3) unsigned NOT NULL,
    CONSTRAINT socialnet_req_post_fk FOREIGN KEY (requestId)
        REFERENCES SocialNetworkRequest(id),
    CONSTRAINT socialnet_resp_post_fk FOREIGN KEY (responseId)
        REFERENCES SocialNetworkResponse(id),
    CONSTRAINT socialnetpost_event_fk FOREIGN KEY (eventId)
        REFERENCES Event(id),
    CONSTRAINT socialnetpost_searchc_fk FOREIGN KEY (searchCriteriaId)
        REFERENCES SearchCriteria(id),
    CONSTRAINT socialnet_post_fk FOREIGN KEY (socialNetworkId)
        REFERENCES SocialNetwork(id),
    CONSTRAINT socialnetwork_status_fk FOREIGN KEY (statusId)
        REFERENCES GenericStatus(id)
);

CREATE TABLE SocialNetworkApiCredential (
    id              bigint(15) unsigned auto_increment PRIMARY KEY,
    socialNetworkId int(4) unsigned NOT NULL,
    appId           varchar(100)    NOT NULL,
    appSecret       varchar(100)    NOT NULL,
    lastRequest     timestamp,
    CONSTRAINT socnet_apicred_fk FOREIGN KEY (socialNetworkId)
        REFERENCES SocialNetwork(id)
);
