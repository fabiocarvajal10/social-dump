package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.SocialNetwork;
import com.coredump.socialdump.repository.SocialNetworkRepository;
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
 * REST controller for managing SocialNetworks.
 */
@RestController
@RequestMapping("/api")
public class SocialNetworkResource {

  private final Logger log =
    LoggerFactory.getLogger(SocialNetworkResource.class);

  @Inject
  private SocialNetworkRepository socialNetworkRepository;

  /**
   * GET  /social-networks -> get all SocialNetworks.
   */
  @RequestMapping(value = "/social-networks",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public List<SocialNetwork> getAll() {
    log.debug("REST request to get all SocialNetworks");
    return socialNetworkRepository.findAll();
  }

  /**
   * GET  /social-networks/:id -> get the "id" generic status.
   */
  @RequestMapping(value = "/social-networks/{id}",
          method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<SocialNetwork> get(
    @PathVariable int id) {
    log.debug("REST request to get SocialNetworks : {}", id);
    return Optional.ofNullable(socialNetworkRepository.findOne(id))
            .map(SocialNetwork ->
                  new ResponseEntity<>(SocialNetwork, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}

