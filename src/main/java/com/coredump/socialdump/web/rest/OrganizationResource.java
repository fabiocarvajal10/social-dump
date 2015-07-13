package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.Organization;
import com.coredump.socialdump.repository.OrganizationRepository;
import com.coredump.socialdump.web.rest.dto.OrganizationDTO;
import com.coredump.socialdump.web.rest.mapper.OrganizationMapper;
import com.coredump.socialdump.web.rest.util.PaginationUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.Valid;

/**
 * Created by fabio on 11/07/15.
 * REST controller for managing organizations
 */
@RestController
@RequestMapping("/api")
public class OrganizationResource {
  private final Logger log = LoggerFactory.getLogger(OrganizationResource.class);

  @Inject
  private OrganizationRepository organizationRepository;

  @Inject
  private OrganizationMapper organizationMapper;

  /**
   * POST  /organizations -> Create a new organization.
   */
  @RequestMapping(value = "/organizations",
          method = RequestMethod.POST,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<Void> create(@Valid @RequestBody OrganizationDTO organizationDTO)
          throws URISyntaxException {
    log.debug("REST request to save Organization : {}", organizationDTO);
    if (organizationDTO.getId() != null) {
      return ResponseEntity.badRequest()
              .header("Failure", "A new organization cannot already have an ID").build();
    }
    organizationDTO.setCreatedAt(DateTime.now());
    Organization organization = organizationMapper.organizationDTOToOrganization(organizationDTO);
    organizationRepository.save(organization);

    return ResponseEntity.created(new URI("/api/organizations/" + organizationDTO.getId())).build();
  }

  /**
   * PUT  /organizations -> Updates an existing organization.
   */
  @RequestMapping(value = "/organizations",
          method = RequestMethod.PUT,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<Void> update(@Valid @RequestParam("id")  Long id,
                                     @RequestParam("name") String name)
          throws URISyntaxException, NullPointerException {
    log.debug("REST request to update Organization name: ", name);
    Organization organization;
    organization = organizationRepository.findOne(id);
    if (organization == null) {
      return ResponseEntity.notFound().build();
    }
    organization.setName(name);
    organizationRepository.save(organization);

    return ResponseEntity.ok().build();
  }

  /**
   * GET  /organizations -> get all the organizations.
   */
  @RequestMapping(value = "/organizations",
          method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  @Transactional(readOnly = true)
  public ResponseEntity<List<OrganizationDTO>> getAll(
          @RequestParam(value = "page" , required = false) Integer offset,
          @RequestParam(value = "per_page", required = false) Integer limit)
          throws URISyntaxException {


    Page<Organization> page = organizationRepository
            .findAllForCurrentUser(PaginationUtil.generatePageRequest(offset, limit));

    HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(page, "/api/organizations", offset, limit);

    return new ResponseEntity<>(page.getContent().stream()
            .map(organizationMapper::organizationToOrganizationDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
  }

  /**
   * GET  /organizations/:id -> get the "id" organization.
   */
  @RequestMapping(value = "/organizations/{id}",
          method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<OrganizationDTO> get(@PathVariable Long id) {
    log.debug("REST request to get Organization : {}", id);
    return Optional.ofNullable(organizationRepository.findOne(id))
            .map(organizationMapper::organizationToOrganizationDTO)
            .map(organizationDTO -> new ResponseEntity<>(
                    organizationDTO,
                    HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  /**
   * DELETE  /organizations/:id -> delete the "id" organization.
   */
  @RequestMapping(value = "/organizations/{id}",
          method = RequestMethod.DELETE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public void delete(@PathVariable Long id) {
    log.debug("REST request to delete Organization : {}", id);
    organizationRepository.delete(id);
  }
}
