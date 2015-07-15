package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.Organization;
import com.coredump.socialdump.repository.OrganizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by fabio on 14/07/15.
 * Service class for managing organizations.
 */
@Service
@Transactional
public class OrganizationService {
  private final Logger log = LoggerFactory.getLogger(OrganizationService.class);

  @Inject
  private OrganizationRepository organizationRepository;

  @Transactional(readOnly = true)
  public Organization ownsOrganization(Long orgId) {
    log.debug("Validating organization owner");
    Organization organization = organizationRepository.findOneForCurrentAndById(orgId);
    if (organization != null) {
      return organization;
    }
    return null;
  }
}
