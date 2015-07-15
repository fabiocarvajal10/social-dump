package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.OrganizationRole;
import com.coredump.socialdump.repository.OrganizationRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing OrganizationRoles.
 */
@RestController
@RequestMapping("/api")
public class OrganizationRoleResource {

  private final Logger log =
    LoggerFactory.getLogger(OrganizationRoleResource.class);

  @Inject
  private OrganizationRoleRepository OrganizationRoleRepository;

  /**
   * GET  /organization-roles -> get all OrganizationRoles.
   */
  @RequestMapping(value = "/organization-roles",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public List<OrganizationRole> getAll() {
    log.debug("REST request to get all OrganizationRoles");
    return OrganizationRoleRepository.findAll();
  }

  /**
   * GET  /organization-roles/:id -> get the "id" generic status.
   */
  @RequestMapping(value = "/organization-roles/{id}",
          method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<OrganizationRole> get(
    @PathVariable short id) {
    log.debug("REST request to get OrganizationRoles : {}", id);
    return Optional.ofNullable(OrganizationRoleRepository.findOne(id))
            .map(OrganizationRole ->
                  new ResponseEntity<>(OrganizationRole, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}

