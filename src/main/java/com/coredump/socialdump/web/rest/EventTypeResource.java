package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.EventType;
import com.coredump.socialdump.repository.EventTypeRepository;
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
 * REST controller for managing EventTypes.
 */
@RestController
@RequestMapping("/api")
public class EventTypeResource {

  private final Logger log =
    LoggerFactory.getLogger(EventTypeResource.class);

  @Inject
  private EventTypeRepository eventTypeRepository;

  /**
   * GET  /event-types -> get all EventTypes.
   */
  @RequestMapping(value = "/event-types",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public List<EventType> getAll() {
    log.debug("REST request to get all EventTypes");
    return eventTypeRepository.findAll();
  }

  /**
   * GET  /event-types/:id -> get the "id" generic status.
   */
  @RequestMapping(value = "/event-types/{id}",
          method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<EventType> get(
    @PathVariable Integer id) {
    log.debug("REST request to get EventTypes : {}", id);
    return Optional.ofNullable(eventTypeRepository.findOne(id))
            .map(EventType ->
                  new ResponseEntity<>(EventType, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}

