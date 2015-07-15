package com.coredump.socialdump.service;
import com.coredump.socialdump.domain.OrganizationRole;
import com.coredump.socialdump.repository.OrganizationRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.inject.Inject;

/**
 * Service class for managing OrganizationRoles.
 */
@Service
@Transactional
public class OrganizationRoleService {

  private final Logger log = LoggerFactory.getLogger(OrganizationRoleService.class);
  @Inject
  private OrganizationRoleRepository OrganizationRoleRepository;

}

