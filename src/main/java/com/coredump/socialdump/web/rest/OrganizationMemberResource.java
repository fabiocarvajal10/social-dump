package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.OrganizationMember;
import com.coredump.socialdump.repository.OrganizationMemberRepository;
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
 * REST controller for managing OrganizationMembers.
 */
@RestController
@RequestMapping("/api")
public class OrganizationMemberResource {

  private final Logger log =
    LoggerFactory.getLogger(OrganizationMemberResource.class);

  @Inject
  private OrganizationMemberRepository organizationMemberRepository;

  /**
   * GET  /organization-members -> get all OrganizationMembers.
   */
  @RequestMapping(value = "/organization-members",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public List<OrganizationMember> getAll() {
    log.debug("REST request to get all OrganizationMembers");
    return organizationMemberRepository.findAll();
  }

  /**
   * GET  /organization-members/:id -> get the "id" generic status.
   */
  @RequestMapping(value = "/organization-members/{id}",
          method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<OrganizationMember> get(
    @PathVariable long id) {
    log.debug("REST request to get OrganizationMembers : {}", id);
    return Optional.ofNullable(organizationMemberRepository.findOne(id))
            .map(OrganizationMember ->
                  new ResponseEntity<>(OrganizationMember, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}

