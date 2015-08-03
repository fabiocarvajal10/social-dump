package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.MonitorContact;
import com.coredump.socialdump.domain.TemporalAccess;
import com.coredump.socialdump.repository.EventRepository;
import com.coredump.socialdump.repository.MonitorContactRepository;
import com.coredump.socialdump.repository.TemporalAccessRepository;
import com.coredump.socialdump.service.GenericStatusService;
import com.coredump.socialdump.service.MailService;
import com.coredump.socialdump.service.util.RandomUtil;
import com.coredump.socialdump.web.rest.dto.TemporalAccessDTO;
import com.coredump.socialdump.web.rest.mapper.TemporalAccessMapper;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TemporalAccesses.
 */
@RestController
@RequestMapping("/api")
public class TemporalAccessResource {

  private final Logger log =
    LoggerFactory.getLogger(TemporalAccessResource.class);

  @Inject
  private TemporalAccessRepository temporalAccessRepository;

  @Inject
  private MonitorContactRepository monitorContactRepository;

  @Inject
  private PasswordEncoder passwordEncoder;

  @Inject
  private EventRepository eventRepository;

  @Inject
  private GenericStatusService genericStatusService;

  @Inject
  private TemporalAccessMapper temporalAccessMapper;

  @Inject
  private MailService mailService;

  /**
   * POST  /register -> register the monitor contact.
   */
  @RequestMapping(value = "/temporal-accesses",
    method = RequestMethod.POST,
    produces = MediaType.TEXT_PLAIN_VALUE)
  @Timed
  public ResponseEntity<?> create(@RequestBody TemporalAccessDTO temporalAccessDTO,
      HttpServletRequest request) {
    TemporalAccess temporalAccess =
        temporalAccessMapper.temporalAccessDTOToTemporalAccess(temporalAccessDTO);

    String password = RandomUtil.generatePassword();
    temporalAccess.setEmail(temporalAccess.getMonitorContactByMonitorContactId().getEmail());
    temporalAccess.setPassword(passwordEncoder.encode(password));
    temporalAccess.setGenericStatusByStatusId(genericStatusService.getActive());
    temporalAccess.setCreatedAt(new DateTime());

    if (temporalAccess.getStartDate() == null && temporalAccess.getEndDate() == null) {
      temporalAccess.setStartDate(temporalAccess.getEventByEventId().getStartDate());
      temporalAccess.setEndDate(temporalAccess.getEventByEventId().getEndDate());
    }

    if (temporalAccess.getStartDate().isBefore(temporalAccess.getEventByEventId().getStartDate())) {
      return new ResponseEntity<>("Monitor cant access before the event",
        HttpStatus.CONFLICT);
    } else if (temporalAccess.getEndDate().isAfter(temporalAccess.getEventByEventId().getEndDate())) {
      return new ResponseEntity<>("Monitor cant access after the event",
        HttpStatus.CONFLICT);
    }

    temporalAccessRepository.save(temporalAccess);

    String baseUrl = request.getScheme() + // "http"
      "://" +                                // "://"
      request.getServerName() +              // "myhost"
      ":" +                                  // ":"
      request.getServerPort();               // "80"

    temporalAccess.setPassword(password);

    mailService.temporalAccessEmail(temporalAccess, baseUrl);

    return new ResponseEntity<>(Long.toString(temporalAccess.getId()), HttpStatus.CREATED);
  }

  /**
   * GET  /temporal-accesses -> get all TemporalAccesses.
   */
  @RequestMapping(value = "/temporal-accesses",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public List<TemporalAccess> getAll(@RequestParam(value = "eventId") Long eventId) {
    log.debug("REST request to get all TemporalAccesses");
    Event event = eventRepository.findOne(eventId);
    return temporalAccessRepository.getAllByEventByEventId(event);
  }

  /**
   * GET  /temporal-accesses/:id -> get the "id" generic status.
   */
  @RequestMapping(value = "/temporal-accesses/{id}",
          method = RequestMethod.GET,
          produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<TemporalAccess> get(@PathVariable long id) {
    log.debug("REST request to get TemporalAccesses : {}", id);
    return Optional.ofNullable(temporalAccessRepository.findOne(id))
            .map(TemporalAccess ->
                  new ResponseEntity<>(TemporalAccess, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  /**
   * DELETE  /temporal-accesses/:id -> delete the temporal access.
   */
  @RequestMapping(value = "/temporal-accesses/{id}",
    method = RequestMethod.DELETE,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  @Transactional
  public ResponseEntity<Void> delete(@PathVariable Long id) {

    TemporalAccess temporalAccess = temporalAccessRepository.findOne(id);

    if (temporalAccess == null) {
      return ResponseEntity.notFound().build();
    } else {
      temporalAccessRepository.delete(temporalAccess);
      return ResponseEntity.ok().build();
    }

  }
}

