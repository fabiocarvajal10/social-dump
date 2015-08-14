package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.OrganizationPrivilege;
import com.coredump.socialdump.repository.OrganizationPrivilegeRepository;
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
 * REST controller for managing OrganizationPrivileges.
 */
@RestController
@RequestMapping("/api")
public class OrganizationPrivilegeResource {

  private final Logger log =
    LoggerFactory.getLogger(OrganizationPrivilegeResource.class);

  @Inject
  private OrganizationPrivilegeRepository organizationPrivilegeRepository;

  /**
   * GET  /organization-privileges -> get all OrganizationPrivileges.
   */
  @RequestMapping(value = "/organization-privileges",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public List<OrganizationPrivilege> getAll() {
    log.debug("REST request to get all OrganizationPrivileges");
    return organizationPrivilegeRepository.findAll();
  }

  /**
   * GET  /organization-privileges/:id -> get the "id" generic status.
   */
  @RequestMapping(value = "/organization-privileges/{id}",
          method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<OrganizationPrivilege> get(
    @PathVariable int id) {
    log.debug("REST request to get OrganizationPrivileges : {}", id);
    return Optional.ofNullable(organizationPrivilegeRepository.findOne(id))
            .map(OrganizationPrivilege ->
                  new ResponseEntity<>(OrganizationPrivilege, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}

