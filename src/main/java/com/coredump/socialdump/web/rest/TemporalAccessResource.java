package com.coredump.socialdump.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coredump.socialdump.domain.MonitorContact;
import com.coredump.socialdump.domain.TemporalAccess;
import com.coredump.socialdump.repository.MonitorContactRepository;
import com.coredump.socialdump.repository.TemporalAccessRepository;
import com.coredump.socialdump.service.GenericStatusService;
import com.coredump.socialdump.web.rest.dto.TemporalAccessDTO;
import com.coredump.socialdump.web.rest.mapper.TemporalAccessMapper;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

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
  private GenericStatusService genericStatusService;

  @Inject
  private TemporalAccessMapper temporalAccessMapper;

  /**
   * POST  /register -> register the monitor contact.
   */
  @RequestMapping(value = "/temporal-accesses",
    method = RequestMethod.POST,
    produces = MediaType.TEXT_PLAIN_VALUE)
  @Timed
  public ResponseEntity<?> create(@RequestBody TemporalAccessDTO temporalAccessDTO) {
      TemporalAccess temporalAccess =
          temporalAccessMapper.temporalAccessDTOToTemporalAccess(temporalAccessDTO);

        temporalAccess.setGenericStatusByStatusId(genericStatusService.getActive());
        temporalAccess.setCreatedAt(new DateTime());

        MonitorContact monitorContact = monitorContactRepository
          .findOne(temporalAccessDTO.getMonitorContactId());

        temporalAccessRepository.save(temporalAccess);
        //enviarEmail

        return new ResponseEntity<>(HttpStatus.CREATED);
  }

  /**
   * GET  /temporal-accesses -> get all TemporalAccesses.
   */
  @RequestMapping(value = "/temporal-accesses",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public List<TemporalAccess> getAll() {
    log.debug("REST request to get all TemporalAccesses");
    return temporalAccessRepository.findAll();
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
}

