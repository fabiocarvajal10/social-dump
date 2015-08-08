package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.SocialNetwork;
import com.coredump.socialdump.domain.SocialNetworkPost;
import com.coredump.socialdump.repository.EventRepository;
import com.coredump.socialdump.repository.SocialNetworkPostRepository;
import com.coredump.socialdump.web.rest.dto.SocialNetworkPostDTO;
import com.coredump.socialdump.web.rest.mapper.SocialNetworkPostMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * REST controller for managing SocialNetworkPosts.
 */
@RestController
@RequestMapping("/api")
public class SocialNetworkPostResource {

  private final Logger log = LoggerFactory
        .getLogger(SocialNetworkPostResource.class);

  @Inject
  private SocialNetworkPostRepository socialNetworkPostRepository;

  @Inject
  private EventRepository eventRepository;

  @Inject
  private SocialNetworkPostMapper socialNetworkPostMapper;

  /**
   * GET  /social-network-posts -> get all SocialNetworkPosts.
   */
  @RequestMapping(value = "/social-network-posts/event/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<List<SocialNetworkPostDTO>> getAll(
        @PathVariable Long id)
        throws URISyntaxException {

    log.debug("REST request to get all SocialNetworkPosts");

    Event event = eventRepository.findOne(id);

    if (event == null) {
      return ResponseEntity.badRequest().body(null);
    }

    List<SocialNetworkPost> posts = socialNetworkPostRepository
          .findByeventByEventId(event);

    return new ResponseEntity<>(posts
          .stream()
          .map(socialNetworkPostMapper::socialNetworkPostToSocialNetworkPostDTO)
          .collect(Collectors.toCollection(LinkedList::new)),
          new HttpHeaders(), HttpStatus.OK);
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

  /**
   * GET  /social-network-posts/:id -> get the "id" generic status.
   */
  @RequestMapping(value = "/social-network-posts/count",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<?> getPostsCountByOrg(
      @RequestParam("organizationId") long organizationId) {

    List<SocialNetwork> postsList =
        socialNetworkPostRepository.findPostsSocialNetworkIdsByOrg(organizationId);

    Map<String, Long> countList =
        postsList.stream()
          .collect(Collectors.groupingBy(p -> p.getName(), Collectors.counting()));

    return new ResponseEntity<>(countList, HttpStatus.OK);
  }
}

