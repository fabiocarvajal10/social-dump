package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.EventStatus;
import com.coredump.socialdump.repository.EventStatusRepository;
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
 * REST controller for managing EventStatuses.
 */
@RestController
@RequestMapping("/api")
public class EventStatusResource {

  private final Logger log =
    LoggerFactory.getLogger(EventStatusResource.class);

  @Inject
  private EventStatusRepository EventStatusRepository;

  /**
   * GET  /event-statuses -> get all EventStatuses.
   */
  @RequestMapping(value = "/event-statuses",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public List<EventStatus> getAll() {
    log.debug("REST request to get all EventStatuses");
    return EventStatusRepository.findAll();
  }

  /**
   * GET  /event-statuses/:id -> get the "id" generic status.
   */
  @RequestMapping(value = "/event-statuses/{id}",
          method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<EventStatus> get(
    @PathVariable Short id) {
    log.debug("REST request to get EventStatuses : {}", id);
    return Optional.ofNullable(EventStatusRepository.findOne(id))
            .map(EventStatus ->
                  new ResponseEntity<>(EventStatus, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}

