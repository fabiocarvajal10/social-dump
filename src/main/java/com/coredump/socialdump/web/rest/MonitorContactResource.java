package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.MonitorContact;
import com.coredump.socialdump.repository.MonitorContactRepository;
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
 * REST controller for managing MonitorContacts.
 */
@RestController
@RequestMapping("/api")
public class MonitorContactResource {

  private final Logger log =
    LoggerFactory.getLogger(MonitorContactResource.class);

  @Inject
  private MonitorContactRepository MonitorContactRepository;

  /**
   * GET  /monitor-contacts -> get all MonitorContacts.
   */
  @RequestMapping(value = "/monitor-contacts",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public List<MonitorContact> getAll() {
    log.debug("REST request to get all MonitorContacts");
    return MonitorContactRepository.findAll();
  }

  /**
   * GET  /monitor-contacts/:id -> get the "id" generic status.
   */
  @RequestMapping(value = "/monitor-contacts/{id}",
          method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<MonitorContact> get(
    @PathVariable long id) {
    log.debug("REST request to get MonitorContacts : {}", id);
    return Optional.ofNullable(MonitorContactRepository.findOne(id))
            .map(MonitorContact ->
                  new ResponseEntity<>(MonitorContact, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}

