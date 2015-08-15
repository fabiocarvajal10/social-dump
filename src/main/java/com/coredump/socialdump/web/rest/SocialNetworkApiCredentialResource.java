package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.SocialNetworkApiCredential;
import com.coredump.socialdump.repository.SocialNetworkApiCredentialRepository;
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
 * REST controller for managing SocialNetworkApiCredentials.
 */
@RestController
@RequestMapping("/api")
public class SocialNetworkApiCredentialResource {

  private final Logger log =
    LoggerFactory.getLogger(SocialNetworkApiCredentialResource.class);

  @Inject
  private SocialNetworkApiCredentialRepository socialNetworkApiCredentialRepository;

  /**
   * GET  /social-network-api-credentials -> get all SocialNetworkApiCredentials.
   */
  @RequestMapping(value = "/social-network-api-credentials",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public List<SocialNetworkApiCredential> getAll() {
    log.debug("REST request to get all SocialNetworkApiCredentials");
    return socialNetworkApiCredentialRepository.findAll();
  }

  /**
   * GET  /social-network-api-credentials/:id -> get the "id" generic status.
   */
  @RequestMapping(value = "/social-network-api-credentials/{id}",
          method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<SocialNetworkApiCredential> get(
    @PathVariable long id) {
    log.debug("REST request to get SocialNetworkApiCredentials : {}", id);
    return Optional.ofNullable(socialNetworkApiCredentialRepository.findOne(id))
            .map(SocialNetworkApiCredential ->
                  new ResponseEntity<>(SocialNetworkApiCredential, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}

