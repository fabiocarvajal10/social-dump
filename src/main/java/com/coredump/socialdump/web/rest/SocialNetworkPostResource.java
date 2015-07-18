package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.SocialNetworkPost;
import com.coredump.socialdump.repository.SocialNetworkPostRepository;
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
 * REST controller for managing SocialNetworkPosts.
 */
@RestController
@RequestMapping("/api")
public class SocialNetworkPostResource {

  private final Logger log =
    LoggerFactory.getLogger(SocialNetworkPostResource.class);

  @Inject
  private SocialNetworkPostRepository socialNetworkPostRepository;

  /**
   * GET  /social-network-posts -> get all SocialNetworkPosts.
   */
  @RequestMapping(value = "/social-network-posts",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public List<SocialNetworkPost> getAll() {
    log.debug("REST request to get all SocialNetworkPosts");
    return socialNetworkPostRepository.findAll();
  }

  /**
   * GET  /social-network-posts/:id -> get the "id" generic status.
   */
  @RequestMapping(value = "/social-network-posts/recent/{id}",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<SocialNetworkPost> getRecentPosts(
    @PathVariable long id) {
    log.debug("REST request to get SocialNetworkPosts : {}", id);
    return Optional.ofNullable(socialNetworkPostRepository.findOne(id))
      .map(SocialNetworkPost ->
        new ResponseEntity<>(SocialNetworkPost, HttpStatus.OK))
      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  /**
   * GET  /social-network-posts/:id -> get the "id" generic status.
   */
  @RequestMapping(value = "/social-network-posts/{id}",
          method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<SocialNetworkPost> get(
    @PathVariable long id) {
    log.debug("REST request to get SocialNetworkPosts : {}", id);
    return Optional.ofNullable(socialNetworkPostRepository.findOne(id))
            .map(SocialNetworkPost ->
                  new ResponseEntity<>(SocialNetworkPost, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

}

