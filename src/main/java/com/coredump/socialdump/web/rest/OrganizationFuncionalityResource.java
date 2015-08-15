package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.OrganizationFuncionality;
import com.coredump.socialdump.repository.OrganizationFuncionalityRepository;
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
 * REST controller for managing OrganizationFuncionalities.
 */
@RestController
@RequestMapping("/api")
public class OrganizationFuncionalityResource {

  private final Logger log =
    LoggerFactory.getLogger(OrganizationFuncionalityResource.class);

  @Inject
  private OrganizationFuncionalityRepository organizationFuncionalityRepository;

  /**
   * GET  /organization-funcionalities -> get all OrganizationFuncionalities.
   */
  @RequestMapping(value = "/organization-funcionalities",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public List<OrganizationFuncionality> getAll() {
    log.debug("REST request to get all OrganizationFuncionalities");
    return organizationFuncionalityRepository.findAll();
  }

  /**
   * GET  /organization-funcionalities/:id -> get the "id" generic status.
   */
  @RequestMapping(value = "/organization-funcionalities/{id}",
          method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<OrganizationFuncionality> get(
    @PathVariable int id) {
    log.debug("REST request to get OrganizationFuncionalities : {}", id);
    return Optional.ofNullable(organizationFuncionalityRepository.findOne(id))
            .map(OrganizationFuncionality ->
                  new ResponseEntity<>(OrganizationFuncionality, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}

