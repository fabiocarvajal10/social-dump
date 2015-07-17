package com.coredump.socialdump.service;
import com.coredump.socialdump.domain.OrganizationPrivilege;
import com.coredump.socialdump.repository.OrganizationPrivilegeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.inject.Inject;

/**
 * Service class for managing OrganizationPrivileges.
 */
@Service
@Transactional
public class OrganizationPrivilegeService {

  private final Logger log = LoggerFactory.getLogger(OrganizationPrivilegeService.class);
  @Inject
  private OrganizationPrivilegeRepository organizationPrivilegeRepository;

}

