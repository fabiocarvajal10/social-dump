package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.Organization;
import com.coredump.socialdump.repository.OrganizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Fabio Carvajal Segura on 14/07/15.
 * Service class for managing organizations.
 */
@Service
@Transactional
public class OrganizationService {
  private final Logger log = LoggerFactory.getLogger(OrganizationService.class);

  @Inject
  private OrganizationRepository organizationRepository;

  /**
   * Método que verifica si un usuario es un dueño de una organización
   * @param orgId id de la organización
   * @return si el usuario es dueño, retorna la organización. En caso contrario retorna null
   */
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
